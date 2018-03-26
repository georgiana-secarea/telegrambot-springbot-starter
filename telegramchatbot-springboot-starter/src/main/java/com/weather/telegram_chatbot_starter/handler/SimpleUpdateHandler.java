package com.weather.telegram_chatbot_starter.handler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.weather.telegram_chatbot_starter.geocoding.ReverseGeocoding;
import com.weather.telegram_chatbot_starter.geocoding.WeatherInfoGather;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.model.PersonLocation;
import com.weather.telegram_chatbot_starter.model.Weather;
import com.weather.telegram_chatbot_starter.model.Location1;
import com.weather.telegram_chatbot_starter.repo.LocationRepo;
import com.weather.telegram_chatbot_starter.repo.PersonLocationRepo;
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

	volatile int currentChatId;
	volatile String currentLocation;

	@Override
	public int process(List<Update> updates) {

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
					insertPerson(chatId);
					KeyboardButton shareLocationButton = new KeyboardButton("Share location");
					shareLocationButton.requestLocation(true);

					KeyboardButton noLocationButton = new KeyboardButton("Deny");

					KeyboardButton[][] buttonsList = new KeyboardButton[1][2];
					buttonsList[0][0] = shareLocationButton;
					buttonsList[0][1] = noLocationButton;

					ReplyKeyboardMarkup shareReplyKeyboard = new ReplyKeyboardMarkup(buttonsList);
					shareReplyKeyboard.resizeKeyboard(true);
					shareReplyKeyboard.oneTimeKeyboard(true);

					sendMessage = new SendMessage(chatId,
							"Hi, I'm WeatherBOT. To receive daily notifications of your favorite location weather, please share your location and phone number.")
									.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
									.replyMarkup(shareReplyKeyboard);
					break;
				}
				case "Deny": {
					ReplyKeyboardMarkup replyKeyboard = showMenu();

					sendMessage = new SendMessage(chatId, "Alright, got it. Now, what would you wish to know?")
							.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(replyKeyboard);
					break;
				}
				default: {
					sendMessage = new SendMessage(chatId, "How may I be at your service?").parseMode(ParseMode.HTML)
							.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
					break;
				}
				}
			} else if (userLocation != null) {

				String location = "Not available";

				ReverseGeocoding revGeo = new ReverseGeocoding();
				try {
					location = revGeo.getCity(userLocation.latitude(), userLocation.longitude());
					// System.out.println("Location after 'Share location': " + location);
					// System.out.println("User id after 'Share location': " + chatId);

				} catch (ApiException | InterruptedException | IOException e) {
					e.printStackTrace();
				}

				KeyboardButton contactButton = new KeyboardButton("Share phone number");
				contactButton.requestContact(true);

				KeyboardButton[][] buttonsList = new KeyboardButton[1][1];
				buttonsList[0][0] = contactButton;

				ReplyKeyboardMarkup contactReplyKeyboard = new ReplyKeyboardMarkup(buttonsList);
				contactReplyKeyboard.resizeKeyboard(true);
				contactReplyKeyboard.oneTimeKeyboard(true);

				sendMessage = new SendMessage(chatId, "Your location has been saved internally: " + location)
						.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
						.replyMarkup(contactReplyKeyboard);

				currentChatId = chatId;
				currentLocation = location;
				insertLocation(location, chatId);

			} else if (userContact != null) {

				insertPerson(userContact);

				Weather currentWeather = new Weather();

				WeatherInfoGather gatherCityWeather = new WeatherInfoGather();
				try {
					currentWeather = gatherCityWeather.getWeather(currentLocation);
				} catch (APIException e) {
					e.printStackTrace();
				}

				sendMessage = new SendMessage(chatId,
						"Your phone number has been saved internally (" + userContact.phoneNumber()
								+ "). Below you can find your location weather and forecast: " + currentWeather)
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(new ForceReply());
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

		KeyboardButton savedLocationWeather = new KeyboardButton("My  location weather");

		KeyboardButton currentLocationWeather = new KeyboardButton("Current weather for current location");
		currentLocationWeather.requestLocation(true);

		KeyboardButton otherLocationWeather = new KeyboardButton("Current weather for another location");

		KeyboardButton currentLocationForecast = new KeyboardButton("Weather forecast for current location");
		currentLocationForecast.requestLocation(true);

		KeyboardButton otherLocationForecast = new KeyboardButton("Weather forecast for another location");

		KeyboardButton userSearchHistory = new KeyboardButton("My search list history");
		userSearchHistory.requestContact(true);

		KeyboardButton[][] buttonsList = new KeyboardButton[3][2];
		buttonsList[0][0] = savedLocationWeather;
		buttonsList[0][1] = currentLocationWeather;
		buttonsList[1][0] = otherLocationWeather;
		buttonsList[1][1] = currentLocationForecast;
		buttonsList[2][0] = otherLocationForecast;
		buttonsList[2][1] = userSearchHistory;

		ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(buttonsList);
		replyKeyboard.resizeKeyboard(true);
		replyKeyboard.oneTimeKeyboard(true);
		return replyKeyboard;
	}

	@Autowired
	PersonRepo personRepo;
	@Autowired
	LocationRepo locationRepo;
	// @Autowired
	// PersonLocationRepo personLocationRepo;

	private void insertPerson(Contact contact) {

		Person person = new Person();
		person.setUserId(contact.userId());
		person.setPhoneNumber(contact.phoneNumber());
		person.setFirstName(contact.firstName());
		person.setLastName(contact.lastName());

		personRepo.save(person);
	}

	private void insertPerson(int chatId) {

		Person person = new Person();
		person.setUserId(chatId);

		personRepo.save(person);
	}

	private void insertLocation(String location, int userId) {

		// insertPerson(userId);
		Optional<Person> person = personRepo.findById(userId);
		Set<Person> persons = new HashSet<Person>();

		if (person.empty() != null) {
			persons.add(person.get());
			locationRepo.save(new Location1(location, persons));
		} else {
			System.out.println("No user found");
		}

	}

	// private void bindLocationToUser(int chatId, String location,boolean
	// isfavorite) {
	// // get location
	// try {
	// Optional<Location1> loc = locationRepo.findByCity(location);
	// LOGGER.info(() -> String.format("Current city %s",currentLocation));
	//
	// if (loc.empty() != null) {
	// personLocationRepo.save(new PersonLocation(chatId, loc.get().getId(),
	// isfavorite));
	// LOGGER.info(() -> String.format("Currenct city id %s",loc.get().getId()));
	// LOGGER.info(() -> String.format("Person id %s",currentChatId));
	// }
	// } catch (Exception e) {
	// System.out.println(e.getStackTrace());
	// }
	//
	// }

}