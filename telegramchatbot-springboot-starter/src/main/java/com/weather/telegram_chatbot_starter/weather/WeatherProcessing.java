package com.weather.telegram_chatbot_starter.weather;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IAdviceDAO;
import com.weather.telegram_chatbot_starter.model.Advice;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.model.Weather;
import com.weather.telegram_chatbot_starter.utils.Utils;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;

@Service
public class WeatherProcessing {

	@Autowired
	private OWM owm;

	@Autowired
	private IAdviceDAO adviceDAO;

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
				currentWeather = getCurrentWeather(locationStr);

				sendMessage = new SendMessage(chatId,
						"Your location has been saved internally, in case you want to check your search history ("
								+ locationStr + ").\n\nBelow you have the current weather information: \n"
								+ currentWeather + "\n\n Would you like to check the forecast for this location too?")
										.parseMode(ParseMode.HTML).disableNotification(false)
										.replyToMessageId(messageId).replyMarkup(Utils.showForecastMenu());

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
	 * This method utilizes the OWM API to collect the current weather information
	 * for the desired location
	 * 
	 * @param city
	 * @return
	 * @throws APIException
	 */
	public Weather getCurrentWeather(String city) throws APIException {

		// getting current weather data for the desired city
		final CurrentWeather cwd = owm.currentWeatherByCityName(city);

		// checking data retrieval was successful or not
		if (cwd.hasRespCode() && cwd.getRespCode() == 200) {

			// checking if city name is available
			if (cwd.hasCityName()) {

				final Advice currentAdvice = adviceDAO.getAdvice(cwd.getWeatherList().get(0).getMainInfo());

				final Weather currentWeather = new Weather();
				currentWeather.setTemperature(cwd.getMainData().getTemp() - 273.15);
				currentWeather.setDescription(cwd.getWeatherList().get(0).getDescription());
				currentWeather.setPressure(cwd.getMainData().getPressure());
				currentWeather.setHumidity(cwd.getMainData().getHumidity());
				currentWeather.setRainfall(cwd.getCloudData().getCloud());
				currentWeather.setAdvice(currentAdvice.getMessage());
				return currentWeather;
			}

		}
		return null;
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

				forecast = getForecast(locationStr);

				String displayForecast = "";
				if (!forecast.isEmpty()) {
					for (Forecast currentHourForecast : forecast)
						displayForecast = displayForecast.concat(currentHourForecast.toString());
				}
				sendMessage = new SendMessage(chatId,
						"Below is the forecast for " + locationStr + ": \n\n" + displayForecast)
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(Utils.showMainMenu());
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
	 * This method utilizes the OWM API to retrieve process the next 3 days forecast
	 * for the desired location
	 * 
	 * @param city
	 * @return
	 * @throws APIException
	 */
	public List<Forecast> getForecast(String city) throws APIException {

		// getting current weather data for the desired city
		final HourlyWeatherForecast forecast = owm.hourlyWeatherForecastByCityName(city);

		// checking data retrieval was successful or not
		if (forecast.hasRespCode() && forecast.getRespCode().equals("200")) {
			// checking if city name is available
			if (forecast.hasDataList()) {

				List<Forecast> forecastList = new ArrayList<Forecast>();
				List<Double> temperatureList = new ArrayList<Double>();
				List<Double> rainfallList = new ArrayList<Double>();
				List<Double> pressureList = new ArrayList<Double>();
				List<Double> humidityList = new ArrayList<Double>();

				int nextDayFirstIndex = ((Utils.DAILY_HOURS - LocalDateTime.now().getHour())
						/ Utils.NEXT_SEARCH_HOURS_INTERVAL);

				List<WeatherData> threeDaysForecast = forecast.getDataList().subList(nextDayFirstIndex,
						nextDayFirstIndex + Utils.THREE_DAYS_FORECAST_SEARCH_COUNT);

				for (int currentDataIndex = 0; currentDataIndex < threeDaysForecast.size(); currentDataIndex++) {

					final WeatherData currentData = threeDaysForecast.get(currentDataIndex);

					temperatureList.add(currentData.getMainData().getTemp() - 273.15);
					rainfallList.add(currentData.getCloudData().getCloud());
					pressureList.add(currentData.getMainData().getPressure());
					humidityList.add(currentData.getMainData().getHumidity());

					if (currentDataIndex != 0 && currentDataIndex % Utils.DAILY_WEATHER_GATHER_COUNT == 0) {
						Double minTemp = temperatureList.stream().mapToDouble(val -> val).min().getAsDouble();
						Double avgTemp = temperatureList.stream().mapToDouble(val -> val).average().getAsDouble();
						Double maxTemp = temperatureList.stream().mapToDouble(val -> val).max().getAsDouble();
						Double avgRainfall = rainfallList.stream().mapToDouble(val -> val).average().getAsDouble();
						Double avgPressure = pressureList.stream().mapToDouble(val -> val).average().getAsDouble();
						Double avgHumidity = humidityList.stream().mapToDouble(val -> val).average().getAsDouble();

						final Forecast currentForecast = new Forecast();
						currentForecast.setDescription(currentData.getWeatherList().get(0).getDescription());
						currentForecast.setMinTemp(minTemp);
						currentForecast.setAvgTemp(avgTemp);
						currentForecast.setMaxTemp(maxTemp);
						currentForecast.setRainfall(avgRainfall);
						currentForecast.setPressure(avgPressure);
						currentForecast.setHumidity(avgHumidity);
						currentForecast.setDate(currentData.getDateTimeText().substring(0, 10));

						forecastList.add(currentForecast);

						temperatureList.clear();
						rainfallList.clear();
						pressureList.clear();
						humidityList.clear();
					}
				}
				return forecastList;
			}
		}
		return null;
	}

	/**
	 * This method retrieves the searched locations history for a desired user
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
					.replyMarkup(Utils.showMainMenu());
		}

		return sendMessage;
	}
}
