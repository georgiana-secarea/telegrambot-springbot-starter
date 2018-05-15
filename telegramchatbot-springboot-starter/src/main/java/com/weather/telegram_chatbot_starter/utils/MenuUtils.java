package com.weather.telegram_chatbot_starter.utils;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public final class MenuUtils {
	
	public final static String START = "/start";
	public final static String DENY = "Deny";
	public final static String FAVORITE_LOCATION = "/fav ";
	public final static String CHOSEN_LOCATION_CURRENT_WEATHER = "/loc ";
	public final static String NOTIFICATION_HOUR = "/hour ";
	public final static String TIMEZONE_CET = "/cet ";
	public final static String SHOW_FORECAST = "Show forecast";
	public final static String ANOTHER_LOCATION_WEATHER_INFO = "Another location weather information";
	public final static String SEARCH_USER_LIST_HISTORY = "My search list history";
	public final static String BACK_TO_MENU = "Go back to menu";
	public final static String APPLICATION_CREDITS = "App credits";
	
	/**
	 * This method generates a menu with the core features of this WeatherBOT
	 * 
	 * @return
	 */
	public static ReplyKeyboardMarkup showMainMenu() {
		final KeyboardButton currentLocationWeather = new KeyboardButton("Current location weather information");
		final KeyboardButton otherLocationWeather = new KeyboardButton("Another location weather information");
		final KeyboardButton userSearchHistory = new KeyboardButton("My search list history");
		final KeyboardButton appCredits = new KeyboardButton("App credits");
		
		currentLocationWeather.requestLocation(true);
		
		final KeyboardButton[][] buttonsList = new KeyboardButton[2][2];
		buttonsList[0][0] = currentLocationWeather;
		buttonsList[0][1] = otherLocationWeather;
		buttonsList[1][0] = userSearchHistory;
		buttonsList[1][1] = appCredits;
		
		final ReplyKeyboardMarkup menuReplyKeyboard = new ReplyKeyboardMarkup(buttonsList);
		menuReplyKeyboard.resizeKeyboard(true);
		menuReplyKeyboard.oneTimeKeyboard(true);
		return menuReplyKeyboard;
	}
	
	/**
	 * This method generates a keyboard markup, that allows the user to whether
	 * check the forecast for the desired location or go back to the main menu
	 *
	 * @return
	 */
	public static ReplyKeyboardMarkup showForecastMenu() {
		final KeyboardButton showForecastButton = new KeyboardButton("Show forecast");
		final KeyboardButton goToMenuButton = new KeyboardButton("Go back to menu");
		
		final KeyboardButton[][] buttonsList = new KeyboardButton[1][2];
		buttonsList[0][0] = showForecastButton;
		buttonsList[0][1] = goToMenuButton;
		
		final ReplyKeyboardMarkup forecastReplyKeyboard = new ReplyKeyboardMarkup(buttonsList);
		forecastReplyKeyboard.resizeKeyboard(true);
		forecastReplyKeyboard.oneTimeKeyboard(true);
		return forecastReplyKeyboard;
	}
	
	/**
	 * This method exposes a two button keyboard for either sharing the contact
	 * details of the user or denying this process
	 * 
	 * @param chatId
	 * @param messageId
	 * @return
	 */
	public static ReplyKeyboardMarkup shareDetailsMenu(Integer chatId, Integer messageId) {
		final KeyboardButton contactButton = new KeyboardButton("Share contact details");
		final KeyboardButton denyButton = new KeyboardButton("Deny");
		contactButton.requestContact(true);
		
		final KeyboardButton[][] buttonsList = new KeyboardButton[1][2];
		buttonsList[0][0] = contactButton;
		buttonsList[0][1] = denyButton;
		
		final ReplyKeyboardMarkup contactDetailsKeyboard = new ReplyKeyboardMarkup(buttonsList);
		contactDetailsKeyboard.resizeKeyboard(true);
		contactDetailsKeyboard.oneTimeKeyboard(true);
		
		return contactDetailsKeyboard;
	}
	
}
