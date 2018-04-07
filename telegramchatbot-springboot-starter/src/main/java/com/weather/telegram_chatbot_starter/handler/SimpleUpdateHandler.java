package com.weather.telegram_chatbot_starter.handler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weather.telegram_chatbot_starter.geocoding.ReverseGeocoding;
import com.weather.telegram_chatbot_starter.geocoding.WeatherInfoGather;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.model.Weather;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.repo.LocationRepo;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.dao.PersonDAO;

import net.aksingh.owmjapis.api.APIException;

import com.google.maps.errors.ApiException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

@Service
public class SimpleUpdateHandler implements UpdatesListener {

	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();

	@Autowired
	private TelegramBot bot;


	@Autowired
	PersonRepo personRepo;

	@Autowired
	LocationRepo locationRepo;
	
	IPersonDAO personDAO;

	ReplyKeyboardMarkup replyKeyboard = showMenu();

	@Override
	public int process(List<Update> updates) {
		personDAO = new PersonDAO(personRepo, locationRepo);
		for (Update update : updates) {
			
			Integer chatId = update.message().from().id();
			String messageText = update.message().text();
			Integer messageId = update.message().messageId();
			Location userLocation = update.message().location();
			Contact userContact = update.message().contact();

			SendMessage sendMessage;

			if (messageText != null) {
				switch (messageText) {
				case "/start": {
					
					Person person = personRepo.findById(chatId);
					LOGGER.info("Favorite city for user "+personDAO.getFavoriteLocationForUser(chatId));
					
					if (person != null && person.getFirstName() != null) {

						sendMessage = new SendMessage(chatId,
								"Welcome back, " + person.getFirstName() + " " + ".What would you wish to check?")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(replyKeyboard);
					} else {
						personDAO.insertPerson(chatId);

						KeyboardButton contactButton = new KeyboardButton("Share contact details");
						KeyboardButton denyButton = new KeyboardButton("Deny");
						contactButton.requestContact(true);

						KeyboardButton[][] buttonsList = new KeyboardButton[1][2];
						buttonsList[0][0] = contactButton;
						buttonsList[0][1] = denyButton;

						ReplyKeyboardMarkup contactReplyKeyboard = new ReplyKeyboardMarkup(buttonsList);
						contactReplyKeyboard.resizeKeyboard(true);
						contactReplyKeyboard.oneTimeKeyboard(true);

						sendMessage = new SendMessage(chatId,
								"Hi, I'm WeatherBOT. To receive daily notifications of your favorite location weather, please share your name, phone number and location.")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(contactReplyKeyboard);
					}
					break;
				}
				case "Deny": {

					sendMessage = new SendMessage(chatId, "Alright, got it. Now, what would you wish to know?")
							.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(replyKeyboard);
					break;
				}
				case "Current weather for another location": {

					sendMessage = new SendMessage(chatId, "Please choose your location first ...")
							.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(new ForceReply());
					break;
				}
				case "Weather forecast for another location": {

					sendMessage = new SendMessage(chatId, "Please choose your location first ...")
							.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(new ForceReply());
					break;
				}
				case "My search list history": {
					
					Set<City> cities = personDAO.getHistoryForUser(chatId);
					String concatCity="";
					for(City c: cities) {
						LOGGER.info(c.getName());
						concatCity +=c.getName()+" ;";
					}
					sendMessage = new SendMessage(chatId, "You may find your search history below:"+concatCity)
							.parseMode(ParseMode.HTML)
							.disableWebPagePreview(true)
							.disableNotification(true)
							.replyToMessageId(messageId)
							.replyMarkup(new ForceReply());
					break;
				}
				case "App credits": {
					sendMessage = new SendMessage(chatId, "WIP")
							.parseMode(ParseMode.HTML)
							.disableNotification(false)
							.replyToMessageId(messageId)
							.replyMarkup(new ForceReply());
					break;
				}
				default: {
					sendMessage = new SendMessage(chatId, "How may I be at your service?")
							.parseMode(ParseMode.HTML)
							.disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(new ForceReply());
					break;
				}
				}
			} else if (userContact != null) {

				personDAO.insertPerson(userContact);

				KeyboardButton shareLocationButton = new KeyboardButton("Share location");
				shareLocationButton.requestLocation(true);

				KeyboardButton[][] buttonsList = new KeyboardButton[1][1];
				buttonsList[0][0] = shareLocationButton;

				ReplyKeyboardMarkup shareReplyKeyboard = new ReplyKeyboardMarkup(buttonsList);
				shareReplyKeyboard.resizeKeyboard(true);
				shareReplyKeyboard.oneTimeKeyboard(true);

				sendMessage = new SendMessage(chatId,
						"Your contact details have been saved internally (" + userContact.phoneNumber()
								+ ").\nNow please share your location.").parseMode(ParseMode.HTML)
										.disableNotification(false).replyToMessageId(messageId)
										.replyMarkup(shareReplyKeyboard);
			} else if (userLocation != null) {

				String location = "Not available";

				ReverseGeocoding revGeo = new ReverseGeocoding();
				try {
					location = revGeo.getCity(userLocation.latitude(), userLocation.longitude());

				} catch (ApiException | InterruptedException | IOException e) {
					e.printStackTrace();
				}

				personDAO.insertLocation(location, chatId);
				
				Weather currentWeather = new Weather();

				 WeatherInfoGather gatherCityWeather = new WeatherInfoGather();
				 try {
				 currentWeather = gatherCityWeather.getWeather(location);
				 } catch (APIException e) {
				 e.printStackTrace();
				 }

				sendMessage = new SendMessage(chatId, "Your location has been saved internally (" + location
						+ ").\nBelow you can find your location current weather, along the other functionalities menu: \r\n"
						+ currentWeather).parseMode(ParseMode.HTML).disableNotification(false)
								.replyToMessageId(messageId).replyMarkup(replyKeyboard);

			} else {
				sendMessage = new SendMessage(chatId, "How may I be at your service?").parseMode(ParseMode.HTML)
						.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
			}

			bot.execute(sendMessage);
		}

		return UpdatesListener.CONFIRMED_UPDATES_ALL;

	}

	/**
	 * This is a menu with the core features of this WeatherBOT
	 * 
	 * @return
	 */
	private ReplyKeyboardMarkup showMenu() {

		final KeyboardButton currentLocationWeather = new KeyboardButton("Current weather for current location");
		currentLocationWeather.requestLocation(true);

		final KeyboardButton otherLocationWeather = new KeyboardButton("Current weather for another location");

		final KeyboardButton currentLocationForecast = new KeyboardButton("Weather forecast for current location");
		currentLocationForecast.requestLocation(true);

		final KeyboardButton otherLocationForecast = new KeyboardButton("Weather forecast for another location");

		final KeyboardButton userSearchHistory = new KeyboardButton("My search list history");
		//userSearchHistory.requestContact(true);

		final KeyboardButton appCredits = new KeyboardButton("App credits");

		final KeyboardButton[][] buttonsList = new KeyboardButton[3][2];
		buttonsList[0][0] = currentLocationWeather;
		buttonsList[0][1] = otherLocationWeather;
		buttonsList[1][0] = currentLocationForecast;
		buttonsList[1][1] = otherLocationForecast;
		buttonsList[2][0] = userSearchHistory;
		buttonsList[2][1] = appCredits;

		final ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(buttonsList);
		replyKeyboard.resizeKeyboard(true);
		return replyKeyboard;
	}



}