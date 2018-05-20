package com.weather.telegram_chatbot_starter.weather;

import java.util.Set;

import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.model.Location;

public interface IWeatherProcessing {
	
	public SendMessage processWeather(Integer chatId, Integer messageId, String locationStr, boolean isNotification);
	
	public SendMessage processForecast(Integer chatId, Integer messageId, String locationStr);
	
	public SendMessage retrieveUserSearchHistory(Integer chatId, Integer messageId, Set<Location> citiesSet);
	
}
