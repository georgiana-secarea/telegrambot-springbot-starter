package com.weather.telegram_chatbot_starter.controller;

import com.weather.telegram_chatbot_starter.service.AnotherLocationWeatherButtonAction;
import com.weather.telegram_chatbot_starter.service.ApplicationCreditsButtonAction;
import com.weather.telegram_chatbot_starter.service.BackToMenuButtonAction;
import com.weather.telegram_chatbot_starter.service.DenyButtonAction;
import com.weather.telegram_chatbot_starter.service.FavoriteLocationCommandAction;
import com.weather.telegram_chatbot_starter.service.ForecastButtonAction;
import com.weather.telegram_chatbot_starter.service.MessageCommandAction;
import com.weather.telegram_chatbot_starter.service.SearchHistoryButtonAction;
import com.weather.telegram_chatbot_starter.service.ShareContactAction;
import com.weather.telegram_chatbot_starter.service.ShareLocationAction;
import com.weather.telegram_chatbot_starter.service.StartCommandAction;
import com.weather.telegram_chatbot_starter.service.UnknownCommandAction;
import com.weather.telegram_chatbot_starter.service.WeatherCommandAction;

public enum InputCommands {

	UNKNOWN_COMMAND("", new UnknownCommandAction()),
	SHARED_CONTACT("", new ShareContactAction()),
	SHARED_LOCATION("", new ShareLocationAction()),
	START_COMMAND("/start", new StartCommandAction()),
	DENY_COMMAND("Deny", new DenyButtonAction()),
	FAV_COMMAND("/fav ", new FavoriteLocationCommandAction()),
	WEATHER_COMMAND("/loc ", new WeatherCommandAction()),
	SHOW_FORECAST_COMMAND("Show forecast", new ForecastButtonAction()),
	ANOTHER_LOCATION_WEATHER_COMMAND("Another location weather information", new AnotherLocationWeatherButtonAction()),
	SEARCH_HISTORY_COMMAND("My search list history", new SearchHistoryButtonAction()),
	BACK_TO_MENU_COMMAND("Go back to menu", new BackToMenuButtonAction()),
	APP_CREDITS_COMMAND("App credits", new ApplicationCreditsButtonAction());

	private String commandText;
	private MessageCommandAction<?> action;

	private InputCommands(String commandText, MessageCommandAction<?> action) {
		this.commandText = commandText;
		this.action = action;
	}

	public String getCommandText() {
		return commandText;
	}

	public MessageCommandAction<?> getAction() {
		return action;
	}

}
