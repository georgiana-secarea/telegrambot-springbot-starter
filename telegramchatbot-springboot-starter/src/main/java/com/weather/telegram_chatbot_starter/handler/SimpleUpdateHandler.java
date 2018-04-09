package com.weather.telegram_chatbot_starter.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.dao.PersonDAO;
import com.weather.telegram_chatbot_starter.geocoding.ReverseGeocoding;
import com.weather.telegram_chatbot_starter.geocoding.WeatherInfoGather;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.model.Weather;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.repo.LocationRepo;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;

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
				if (messageText.startsWith("/fav ")) {

					String[] inputLocation = messageText.split("/fav ", 2);
					String favLocation = inputLocation[1];

					try {

						Weather currentWeather = new Weather();

						WeatherInfoGather gatherCityWeather = new WeatherInfoGather();

						currentWeather = gatherCityWeather.getCurrentWeather(favLocation);

						personDAO.insertFavoriteLocation(favLocation, chatId);

						sendMessage = new SendMessage(chatId,
								"Your location has been saved internally for notification purposes (" + inputLocation[1]
										+ ").\nBy the way, since you were probably too busy looking at your phone, here is some weather information about your location: "
										+ currentWeather).parseMode(ParseMode.HTML).disableNotification(false)
												.replyToMessageId(messageId).replyMarkup(showMenu());
					} catch (APIException e) {
						sendMessage = new SendMessage(chatId,
								"Your location could not be processed. Please make sure you enter a valid one!")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(new ForceReply());
					}
				} else if (messageText.startsWith("/loc ")) {
					String[] inputLocation = messageText.split("/loc ", 2);

					String locationStr = inputLocation[1];

					sendMessage = processWeatherAndForecast(chatId, messageId, locationStr);
				} else {
					switch (messageText) {
					case "/start": {

						Person person = personRepo.findById(chatId);
						LOGGER.info("Favorite city for user " + personDAO.getFavoriteLocationForUser(chatId));
						if (person != null && person.getFirstName() != null) {

							sendMessage = new SendMessage(chatId,
									"Welcome back, " + person.getFirstName() + " " + person.getLastName()
											+ ". What would you wish to check?").parseMode(ParseMode.HTML)
													.disableNotification(false).replyToMessageId(messageId)
													.replyMarkup(showMenu());
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
					case "Another location weather information": {

						sendMessage = new SendMessage(chatId,
								"Please choose your location first, by using the format /loc yourLocation")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(new ForceReply());
						break;
					}
					case "My search list history": {
						Set<City> cities = personDAO.getHistoryForUser(chatId);
						String citiesList = "";
						if (cities != null && !cities.isEmpty()) {
							for (City c : cities) {
								citiesList = citiesList.concat(c.getName() + " | ");
							}
							sendMessage = new SendMessage(chatId,
									"Your search list history is the following one: " + citiesList)
											.parseMode(ParseMode.HTML).disableWebPagePreview(true)
											.disableNotification(true).replyToMessageId(messageId)
											.replyMarkup(new ForceReply());
						} else {
							sendMessage = new SendMessage(chatId, "You didn't search any location until now!")
									.parseMode(ParseMode.HTML).disableWebPagePreview(true).disableNotification(true)
									.replyToMessageId(messageId).replyMarkup(new ForceReply());
						}

						break;
					}
					case "App credits": {
						sendMessage = new SendMessage(chatId, "WIP").parseMode(ParseMode.HTML)
								.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
						break;
					}
					default: {
						sendMessage = new SendMessage(chatId, "How may I be at your service?").parseMode(ParseMode.HTML)
								.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
						break;
					}
					}
				}
			} else if (userContact != null) {

				personDAO.insertPerson(userContact);

				sendMessage = new SendMessage(chatId,
						"Your contact details have been saved internally (" + userContact.phoneNumber()
								+ ").\nNow please enter your location using the format \"/fav yourLocation\"")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(new ForceReply());
			} else if (userLocation != null) {

				String location = "Not available";

				ReverseGeocoding revGeo = new ReverseGeocoding();
				try {
					location = revGeo.getCity(userLocation.latitude(), userLocation.longitude());

				} catch (ApiException | InterruptedException | IOException e) {
					e.printStackTrace();
				}

				sendMessage = processWeatherAndForecast(chatId, messageId, location);

			} else {
				sendMessage = new SendMessage(chatId, "How may I be at your service?").parseMode(ParseMode.HTML)
						.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
			}

			bot.execute(sendMessage);
		}

		return UpdatesListener.CONFIRMED_UPDATES_ALL;

	}

	private SendMessage processWeatherAndForecast(Integer chatId, Integer messageId, String locationStr) {

		personDAO.insertLocation(locationStr, chatId);

		WeatherInfoGather gatherCityWeather = new WeatherInfoGather();
		SendMessage sendMessage;
		Weather currentWeather;
		List<Forecast> forecast;
		try {

			if (locationStr != null) {
				currentWeather = gatherCityWeather.getCurrentWeather(locationStr);

				forecast = gatherCityWeather.getForecast(locationStr);

				sendMessage = new SendMessage(chatId,
						"Your location has been saved internally in case you want to check your search history ("
								+ locationStr + ").\n\nBelow you have the current weather information: \n"
								+ currentWeather + "\n\n").parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(new ForceReply());

				bot.execute(sendMessage);

				String displayForecast = "";
				if (!forecast.isEmpty()) {
					for (Forecast currentHourForecast : forecast)
						displayForecast = displayForecast.concat(currentHourForecast.toString());
				}
				sendMessage = new SendMessage(chatId,
						"You may also find the next 3 days forecast below: \n" + displayForecast)
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(new ForceReply());
			} else
				sendMessage = new SendMessage(chatId,
						"You must enter the required format to receive the weather information!")
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(new ForceReply());
		} catch (APIException e) {
			sendMessage = new SendMessage(chatId,
					"Your location could not be processed. Please make sure you enter a valid one!")
							.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(new ForceReply());
		}
		return sendMessage;
	}

	/**
	 * This is a menu with the core features of this WeatherBOT
	 * 
	 * @return
	 */
	private ReplyKeyboardMarkup showMenu() {

		final KeyboardButton currentLocationWeather = new KeyboardButton("Current location weather information");
		currentLocationWeather.requestLocation(true);

		final KeyboardButton otherLocationWeather = new KeyboardButton("Another location weather information");

		final KeyboardButton userSearchHistory = new KeyboardButton("My search list history");

		final KeyboardButton appCredits = new KeyboardButton("App credits");

		final KeyboardButton[][] buttonsList = new KeyboardButton[2][2];
		buttonsList[0][0] = currentLocationWeather;
		buttonsList[0][1] = otherLocationWeather;
		buttonsList[1][0] = userSearchHistory;
		buttonsList[1][1] = appCredits;

		final ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(buttonsList);
		replyKeyboard.resizeKeyboard(true);
		replyKeyboard.oneTimeKeyboard(true);
		return replyKeyboard;
	}

	// /**
	// * This is an inline keyboard markup, that allows the user to check what
	// weather
	// * information is desired
	// *
	// * @return
	// */
	// private ReplyKeyboardMarkup showWeatherTypes() {
	//
	// final KeyboardButton currentLocationWeather = new KeyboardButton("Current
	// weather");
	//
	// final KeyboardButton currentLocationForecast = new
	// KeyboardButton("Forecast");
	//
	// final KeyboardButton cancelRequest = new KeyboardButton("Cancel");
	//
	// final KeyboardButton[][] buttonsList = new KeyboardButton[1][3];
	// buttonsList[0][0] = currentLocationWeather;
	// buttonsList[0][1] = currentLocationForecast;
	// buttonsList[0][2] = cancelRequest;
	//
	// final ReplyKeyboardMarkup replyKeyboard = new
	// ReplyKeyboardMarkup(buttonsList);
	// replyKeyboard.resizeKeyboard(true);
	// replyKeyboard.oneTimeKeyboard(true);
	// return replyKeyboard;
	// }

}