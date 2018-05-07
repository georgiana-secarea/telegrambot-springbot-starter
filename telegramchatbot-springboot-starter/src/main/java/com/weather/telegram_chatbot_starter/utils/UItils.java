package com.weather.telegram_chatbot_starter.utils;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

public final class UItils {

	// Define the token (key) for the various API(s) utilized throughout this
	// project
	public static final String BOT_API_TOKEN = "582035472:AAGkvIK1LClMwmrAFgo6Ocgk_h936LINv6k";
	public static final String WEATHER_API_TOKEN = "e3e4e932f1805e307be401fdbedf21a3";
	public static final String GEOCODING_API_TOKEN = "AIzaSyD2vVD-ZgpKbAvjtk158jIu72UwYm44fp0";

	// Define multiple values for the algorithm which processes the next 3 days
	// forecast
	public static final int THREE_DAYS_FORECAST_SEARCH_COUNT = 24;
	public static final int NEXT_SEARCH_HOURS_INTERVAL = 3;
	public static final int DAILY_HOURS = 24;
	public static final int DAILY_WEATHER_GATHER_COUNT = 7;

	// Define the set of commands available on the UI
	public static final String START_COMMAND = "/start";
	public static final String DENY_ACTION = "Deny";
	public static final String FAVORITE_LOCATION = "/fav ";
	public static final String CHOSEN_LOCATION_CURRENT_WEATHER = "/loc ";
	public static final String SHOW_FORECAST = "Show forecast";
	public static final String ANOTHER_LOCATION_WEATHER_INFO = "Another location weather information";
	public static final String SEARCH_USER_LIST_HISTORY = "My search list history";
	public static final String BACK_TO_MENU = "Go back to menu";
	public static final String APPLICATION_CREDITS = "App credits";

	// Define the user desired location
	public static String locationStr = "";

	/**
	 * This is a menu with the core features of this WeatherBOT
	 * 
	 * @return
	 */
	public static ReplyKeyboardMarkup showMainMenu() {
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
	public static ReplyKeyboardMarkup showForecastMenu() {
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
	public static SendMessage shareDetailsMenu(Integer chatId, Integer messageId) {
		SendMessage sendMessage;
		final KeyboardButton contactButton = new KeyboardButton("Share contact details");
		final KeyboardButton denyButton = new KeyboardButton("Deny");
		contactButton.requestContact(true);

		final KeyboardButton[][] buttonsList = new KeyboardButton[1][2];
		buttonsList[0][0] = contactButton;
		buttonsList[0][1] = denyButton;

		final ReplyKeyboardMarkup contactReplyKeyboard = new ReplyKeyboardMarkup(buttonsList);
		contactReplyKeyboard.resizeKeyboard(true);
		contactReplyKeyboard.oneTimeKeyboard(true);

		sendMessage = new SendMessage(chatId,
				"Hi, I'm WeatherBOT. To receive daily notifications of your favorite location weather,"
						+ " please share your name, phone number and location.").parseMode(ParseMode.HTML)
								.disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(contactReplyKeyboard);
		return sendMessage;
	}

}
