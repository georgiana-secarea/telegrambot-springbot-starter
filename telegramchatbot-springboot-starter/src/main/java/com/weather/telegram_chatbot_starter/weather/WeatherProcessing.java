package com.weather.telegram_chatbot_starter.weather;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.config.Properties;
import com.weather.telegram_chatbot_starter.dao.IAdviceDAO;
import com.weather.telegram_chatbot_starter.dao.IMessageDAO;
import com.weather.telegram_chatbot_starter.model.Location;
import com.weather.telegram_chatbot_starter.model.Advice;
import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.model.Weather;
import com.weather.telegram_chatbot_starter.utils.MenuUtils;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;

@Service
public class WeatherProcessing implements IWeatherProcessing {
	
	@Autowired
	private OWM owm;
	
	@Autowired
	private IAdviceDAO adviceDAO;
	
	@Autowired
	private IMessageDAO messageDAO;
	
	@Autowired
	private Properties properties;
	
	@Autowired
	private MenuUtils menuUtils;
	
	/**
	 * This method processes the current weather information based on the input
	 * location
	 * 
	 * @param chatId
	 * @param messageId
	 * @param locationStr
	 * @return
	 */
	public SendMessage processWeather(Integer chatId, Integer messageId, String locationStr, boolean saveLocation) {
		SendMessage sendMessage;
		
		if (locationStr == null) {
			sendMessage = new SendMessage(chatId, String.format(messageDAO.getMessage("noLocationReceived")))
					.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
					.replyMarkup(menuUtils.showForecastMenu());
		} else {
			try {
				final Weather currentWeather = getCurrentWeather(locationStr);
				
				if (saveLocation) {
					sendMessage = new SendMessage(chatId,
							String.format(messageDAO.getMessage("weatherSavedLocation"), locationStr,
									currentWeather.toString())).parseMode(ParseMode.HTML).disableNotification(false)
											.replyToMessageId(messageId).replyMarkup(menuUtils.showForecastMenu());
				} else {
					sendMessage = new SendMessage(chatId,
							String.format(messageDAO.getMessage("weatherNoSave"), locationStr,
									currentWeather.toString())).parseMode(ParseMode.HTML).disableNotification(false)
											.replyMarkup(menuUtils.showForecastMenu());
				}
			} catch (final APIException e) {
				sendMessage = new SendMessage(chatId, String.format(messageDAO.getMessage("processingException")))
						.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
						.replyMarkup(menuUtils.showMainMenu());
			}
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
	private Weather getCurrentWeather(String city) throws APIException {
		
		// getting current weather data for the desired city
		final CurrentWeather cwd = owm.currentWeatherByCityName(city);
		
		// checking data retrieval was successful or not
		if (cwd.hasRespCode() && cwd.getRespCode() == 200) {
			
			// checking if city name is available
			if (cwd.hasCityName()) {
				
				String adviceEmoji;
				String adviceMessage;
				
				final Advice adviceObj = adviceDAO.getAdvice(cwd.getWeatherList().get(0).getMainInfo());
				if (adviceObj == null) {
					adviceEmoji = ":no_entry_sign:";
					adviceMessage = "Condition unknown!";
				} else {
					adviceEmoji = adviceObj.getEmoji();
					adviceMessage = adviceObj.getMessage();
				}
				
				final Weather currentWeather = new Weather();
				currentWeather.setTemperature(cwd.getMainData().getTemp() - 273.15);
				currentWeather.setDescription(cwd.getWeatherList().get(0).getDescription());
				currentWeather.setPressure(cwd.getMainData().getPressure());
				currentWeather.setHumidity(cwd.getMainData().getHumidity());
				currentWeather.setRainfall(cwd.getCloudData().getCloud());
				currentWeather.setStatusEmoji(adviceEmoji);
				currentWeather.setAdvice(adviceMessage);
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
		
		try {
			
			if (locationStr != null && !locationStr.equals("")) {
				
				final List<Forecast> forecast = getForecast(locationStr);
				
				String displayForecast = "";
				if (!forecast.isEmpty()) {
					for (Forecast currentHourForecast : forecast)
						displayForecast = displayForecast.concat(currentHourForecast.toString());
				}
				sendMessage = new SendMessage(chatId,
						String.format(messageDAO.getMessage("forecast"), locationStr, displayForecast))
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(menuUtils.showMainMenu());
			} else
				sendMessage = new SendMessage(chatId, String.format(messageDAO.getMessage("noLocationReceived")))
						.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
						.replyMarkup(menuUtils.showMainMenu());
		} catch (APIException e) {
			sendMessage = new SendMessage(chatId, String.format(messageDAO.getMessage("processingException")))
					.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
					.replyMarkup(menuUtils.showMainMenu());
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
	private List<Forecast> getForecast(String city) throws APIException {
		
		// Getting current weather data for the desired city
		final HourlyWeatherForecast forecast = owm.hourlyWeatherForecastByCityName(city);
		
		// Checking whether data retrieval was successful or not
		if (forecast.hasRespCode() && forecast.getRespCode().equals("200")) {
			// Cecking if city name is available
			if (forecast.hasDataList()) {
				
				final int nextDayFirstIndex = ((properties.getDailyHours() - LocalDateTime.now().getHour())
						/ properties.getNextSearchHoursInterval() + 1);
				
				final List<WeatherData> nextDaysForecast = forecast.getDataList().subList(0,
						nextDayFirstIndex + properties.getNextThreeDaysEntriesCount());
				
				final List<Forecast> forecastList = new ArrayList<Forecast>();
				final List<Double> temperatureList = new ArrayList<Double>();
				final List<Double> rainfallList = new ArrayList<Double>();
				final List<Double> pressureList = new ArrayList<Double>();
				final List<Double> humidityList = new ArrayList<Double>();
				
				// Generate the forecast data for today's indexes
				for (int currentDataIndex = 0; currentDataIndex < nextDayFirstIndex; currentDataIndex++) {
					
					final WeatherData currentData = nextDaysForecast.get(currentDataIndex);
					
					temperatureList.add(currentData.getMainData().getTemp() - 273.15);
					rainfallList.add(currentData.getCloudData().getCloud());
					pressureList.add(currentData.getMainData().getPressure());
					humidityList.add(currentData.getMainData().getHumidity());
					
					if (currentDataIndex != 0 && currentDataIndex == nextDayFirstIndex - 1) {
						forecastList.add(calculateForecastData(temperatureList, rainfallList, pressureList,
								humidityList, currentData));
					}
				}
				
				// Generate the forecast data for the next 3 days indexes
				for (int currentDataIndex = nextDayFirstIndex; currentDataIndex < nextDaysForecast
						.size(); currentDataIndex++) {
					
					final WeatherData currentData = nextDaysForecast.get(currentDataIndex);
					
					temperatureList.add(currentData.getMainData().getTemp() - 273.15);
					rainfallList.add(currentData.getCloudData().getCloud());
					pressureList.add(currentData.getMainData().getPressure());
					humidityList.add(currentData.getMainData().getHumidity());
					
					if (currentDataIndex != 0 && currentDataIndex % properties.getWeatherGatherCount() == 0) {
						forecastList.add(calculateForecastData(temperatureList, rainfallList, pressureList,
								humidityList, currentData));
					}
				}
				return forecastList;
			}
		}
		return null;
	}
	
	/**
	 * This method calculates the forecast data for a specified weather list that
	 * includes daily weather information
	 * 
	 * @param temperatureList
	 * @param rainfallList
	 * @param pressureList
	 * @param humidityList
	 * @param currentData
	 * @return
	 */
	private Forecast calculateForecastData(final List<Double> temperatureList, final List<Double> rainfallList,
			final List<Double> pressureList, final List<Double> humidityList, final WeatherData currentData) {
		
		final Double minTemp = temperatureList.stream().mapToDouble(val -> val).min().getAsDouble();
		final Double avgTemp = temperatureList.stream().mapToDouble(val -> val).average().getAsDouble();
		final Double maxTemp = temperatureList.stream().mapToDouble(val -> val).max().getAsDouble();
		final Double avgRainfall = rainfallList.stream().mapToDouble(val -> val).average().getAsDouble();
		final Double avgPressure = pressureList.stream().mapToDouble(val -> val).average().getAsDouble();
		final Double avgHumidity = humidityList.stream().mapToDouble(val -> val).average().getAsDouble();
		
		final Forecast currentForecast = new Forecast();
		currentForecast.setDescription(currentData.getWeatherList().get(0).getDescription());
		currentForecast.setMinTemp(minTemp);
		currentForecast.setAvgTemp(avgTemp);
		currentForecast.setMaxTemp(maxTemp);
		currentForecast.setRainfall(avgRainfall);
		currentForecast.setPressure(avgPressure);
		currentForecast.setHumidity(avgHumidity);
		currentForecast.setDate(currentData.getDateTimeText().substring(0, 10));
		
		temperatureList.clear();
		rainfallList.clear();
		pressureList.clear();
		humidityList.clear();
		
		return currentForecast;
	}
	
	/**
	 * This method retrieves the searched locations history for a desired user
	 * 
	 * @param chatId
	 * @param messageId
	 * @param citiesSet
	 * @return
	 */
	public SendMessage retrieveUserSearchHistory(Integer chatId, Integer messageId, Set<Location> citiesSet) {
		SendMessage sendMessage;
		String citiesList = "";
		
		if (citiesSet != null && !citiesSet.isEmpty()) {
			for (Location currentCity : citiesSet) {
				citiesList = citiesList.concat("• " + currentCity.getName() + "\n");
			}
			sendMessage = new SendMessage(chatId,
					String.format(messageDAO.getMessage("userSearchedLocationsList"), citiesList))
							.parseMode(ParseMode.HTML).disableWebPagePreview(true).disableNotification(true)
							.replyToMessageId(messageId).replyMarkup(menuUtils.showMainMenu());
		} else {
			sendMessage = new SendMessage(chatId, messageDAO.getMessage("noLocationSearched")).parseMode(ParseMode.HTML)
					.disableWebPagePreview(true).disableNotification(true).replyToMessageId(messageId)
					.replyMarkup(menuUtils.showMainMenu());
		}
		
		return sendMessage;
	}
}
