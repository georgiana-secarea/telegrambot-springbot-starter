package com.weather.telegram_chatbot_starter.handler;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.dao.PersonDAO;
import com.weather.telegram_chatbot_starter.geocoding.ReverseGeocoding;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.repo.LocationRepo;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;

import com.google.maps.errors.ApiException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
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

	String locationStr = "";

	ReverseGeocoding revGeo = new ReverseGeocoding();

	HandlerUtils handlerUtils = new HandlerUtils();

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

					personDAO.insertFavoriteLocation(favLocation, chatId);

					sendMessage = handlerUtils.processWeather(chatId, messageId, favLocation);

				} else if (messageText.startsWith("/loc ")) {
					String[] inputLocation = messageText.split("/loc ", 2);
					locationStr = inputLocation[1];

					personDAO.insertLocation(locationStr, chatId);

					sendMessage = handlerUtils.processWeather(chatId, messageId, locationStr);
				} else {
					switch (messageText) {
					case "/start": {
						Person person = personRepo.findById(chatId);

						if (person != null && person.getFirstName() != null) {

							sendMessage = new SendMessage(chatId,
									"Welcome back, " + person.getFirstName() + " " + person.getLastName()
											+ ". What would you wish to check?").parseMode(ParseMode.HTML)
													.disableNotification(false).replyToMessageId(messageId)
													.replyMarkup(handlerUtils.showMainMenu());
						} else {
							personDAO.insertPerson(chatId);

							sendMessage = handlerUtils.shareDetailsMenu(chatId, messageId);
						}
						break;
					}

					case "Deny": {
						sendMessage = new SendMessage(chatId, "Alright, got it. Now, what would you wish to know?")
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(handlerUtils.showMainMenu());
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

						sendMessage = handlerUtils.retrieveUserSearchHistory(chatId, messageId, cities);

						break;
					}
					case "Show forecast": {
						sendMessage = handlerUtils.processForecast(chatId, messageId, locationStr);

						break;
					}
					case "Go back to menu": {
						sendMessage = new SendMessage(chatId, "Below you can find the main menu.")
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(handlerUtils.showMainMenu());
						break;
					}
					case "App credits": {
						sendMessage = new SendMessage(chatId,
								"Developers: Georgiana Secarea and Mircea Stan\n "
										+ "Special thanks to Vlad for his continuous support throughout this project!")
												.parseMode(ParseMode.HTML).disableNotification(false)
												.replyToMessageId(messageId).replyMarkup(new ForceReply());
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

				sendMessage = new SendMessage(chatId, "Your contact details have been saved internally ("
						+ userContact.phoneNumber()
						+ ").\nNow please enter your favorite location where you want your notifications to be received"
						+ ", using the format \"/fav yourLocation\"").parseMode(ParseMode.HTML)
								.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
			} else if (userLocation != null) {
				String location = "Not available";

				try {
					location = revGeo.getCity(userLocation.latitude(), userLocation.longitude());

				} catch (ApiException | InterruptedException | IOException e) {
					e.printStackTrace();
				}

				personDAO.insertLocation(location, chatId);

				sendMessage = handlerUtils.processWeather(chatId, messageId, location);

			} else {
				sendMessage = new SendMessage(chatId, "How may I be at your service?").parseMode(ParseMode.HTML)
						.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
			}

			bot.execute(sendMessage);
		}

		return UpdatesListener.CONFIRMED_UPDATES_ALL;
	}

}