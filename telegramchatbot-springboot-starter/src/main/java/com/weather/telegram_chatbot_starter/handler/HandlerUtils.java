package com.weather.telegram_chatbot_starter.handler;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IAdviceDAO;
import com.weather.telegram_chatbot_starter.geocoding.WeatherInfoGather;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.model.Weather;
import net.aksingh.owmjapis.api.APIException;

@Service
public class HandlerUtils {

	@Autowired
	WeatherInfoGather gatherCityWeather;

	/**
	 * This is a menu with the core features of this WeatherBOT
	 * 
	 * @return
	 */
	public ReplyKeyboardMarkup showMainMenu() {
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

	/**
	 * This is an inline keyboard markup, that allows the user to whether check the
	 *
	 * @return
	 */
	public ReplyKeyboardMarkup showForecastMenu() {
		final KeyboardButton showForecastButton = new KeyboardButton("Show forecast");
		final KeyboardButton goToMenuButton = new KeyboardButton("Go back to menu");

		final KeyboardButton[][] buttonsList = new KeyboardButton[1][2];
		buttonsList[0][0] = showForecastButton;
		buttonsList[0][1] = goToMenuButton;

		final ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(buttonsList);
		replyKeyboard.resizeKeyboard(true);
		replyKeyboard.oneTimeKeyboard(true);
		return replyKeyboard;
	}

	/**
	 * This method exposes a two button keyboard for either sharing the contact
	 * details of the user or denying this process
	 * 
	 * @param chatId
	 * @param messageId
	 * @return
	 */
	public SendMessage shareDetailsMenu(Integer chatId, Integer messageId) {
		SendMessage sendMessage;
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
				"Hi, I'm WeatherBOT. To receive daily notifications of your favorite location weather,"
						+ " please share your name, phone number and location.").parseMode(ParseMode.HTML)
								.disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(contactReplyKeyboard);
		return sendMessage;
	}

	/**
	 * This method processes the current weather information based on the input
	 * location
	 * 
	 * @param chatId
	 * @param messageId
	 * @param locationStr
	 * @return
	 */
	public SendMessage processWeather(Integer chatId, Integer messageId, String locationStr, IAdviceDAO adviceDAO) {
		SendMessage sendMessage;
		Weather currentWeather;
		try {

			if (locationStr != null) {
				currentWeather = gatherCityWeather.getCurrentWeather(locationStr, adviceDAO);

				sendMessage = new SendMessage(chatId,
						"Your location has been saved internally in case you want to check your search history ("
								+ locationStr + ").\n\nBelow you have the current weather information: \n"
								+ currentWeather + "\n\n Would you like to check the forecast for this location too?")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(showForecastMenu());

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
	 * This method processes the weather forecast for the next 3 days based on the
	 * input location
	 * 
	 * @param chatId
	 * @param messageId
	 * @param locationStr
	 * @return
	 */
	public SendMessage processForecast(Integer chatId, Integer messageId, String locationStr) {
		SendMessage sendMessage;
		List<Forecast> forecast;
		try {

			if (locationStr != null && !locationStr.equals("")) {

				forecast = gatherCityWeather.getForecast(locationStr);

				String displayForecast = "";
				if (!forecast.isEmpty()) {
					for (Forecast currentHourForecast : forecast)
						displayForecast = displayForecast.concat(currentHourForecast.toString());
				}
				sendMessage = new SendMessage(chatId,
						"Below is the forecast for " + locationStr + ": \n\n" + displayForecast)
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(showMainMenu());
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
	 * 
	 * @param chatId
	 * @param messageId
	 * @param citiesSet
	 * @return
	 */
	public SendMessage retrieveUserSearchHistory(Integer chatId, Integer messageId, Set<City> citiesSet) {
		SendMessage sendMessage;
		String citiesList = "";

		if (citiesSet != null && !citiesSet.isEmpty()) {
			for (City currentCity : citiesSet) {
				citiesList = citiesList.concat(currentCity.getName() + "; ");
			}

			sendMessage = new SendMessage(chatId, "Your search list history is the following one: " + citiesList)
					.parseMode(ParseMode.HTML).disableWebPagePreview(true).disableNotification(true)
					.replyToMessageId(messageId).replyMarkup(new ForceReply());
		} else {
			sendMessage = new SendMessage(chatId, "You didn't search any location until now!").parseMode(ParseMode.HTML)
					.disableWebPagePreview(true).disableNotification(true).replyToMessageId(messageId)
					.replyMarkup(new ForceReply());
		}

		return sendMessage;
	}

}
