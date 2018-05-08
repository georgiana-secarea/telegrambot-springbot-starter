package com.weather.telegram_chatbot_starter.handler;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.geocoding.WeatherInfoGather;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.model.Weather;
import com.weather.telegram_chatbot_starter.utils.BotUtils;

import net.aksingh.owmjapis.api.APIException;

@Service
public class HandlerUtils {

	@Autowired
	private WeatherInfoGather gatherCityWeather;

	@Autowired
	private GeoApiContext context;

	/**
	 * This method processes the current weather information based on the input
	 * location
	 * 
	 * @param chatId
	 * @param messageId
	 * @param locationStr
	 * @return
	 */
	public SendMessage processWeather(Integer chatId, Integer messageId, String locationStr) {
		SendMessage sendMessage;
		Weather currentWeather;
		try {

			if (locationStr != null) {
				currentWeather = gatherCityWeather.getCurrentWeather(locationStr);

				sendMessage = new SendMessage(chatId,
						"Your location has been saved internally, in case you want to check your search history ("
								+ locationStr + ").\n\nBelow you have the current weather information: \n"
								+ currentWeather + "\n\n Would you like to check the forecast for this location too?")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(BotUtils.showForecastMenu());

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
								.replyMarkup(BotUtils.showMainMenu());
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
					.replyMarkup(BotUtils.showMainMenu());
		}

		return sendMessage;
	}

	public String getCity(double lat, double lng) throws ApiException, InterruptedException, IOException {

		final GeocodingResult[] results = GeocodingApi.newRequest(context).latlng(new LatLng(lat, lng)).language("en")
				.resultType(AddressType.COUNTRY, AddressType.ADMINISTRATIVE_AREA_LEVEL_2).await();

		final String city = results[0].addressComponents[0].longName;

		return city;
	}

}
