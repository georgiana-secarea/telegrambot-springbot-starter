package com.weather.telegram_chatbot_starter.geocoding;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.telegram_chatbot_starter.dao.IAdviceDAO;
import com.weather.telegram_chatbot_starter.model.Advice;
import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.model.Weather;
import com.weather.telegram_chatbot_starter.utils.BotUtils;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;

@Service
public class WeatherInfoGather {

	@Autowired
	private OWM owm;

	@Autowired
	private IAdviceDAO adviceDAO;

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

				int nextDayFirstIndex = ((BotUtils.DAILY_HOURS - LocalDateTime.now().getHour())
						/ BotUtils.NEXT_SEARCH_HOURS_INTERVAL);

				List<WeatherData> threeDaysForecast = forecast.getDataList().subList(nextDayFirstIndex,
						nextDayFirstIndex + BotUtils.THREE_DAYS_FORECAST_SEARCH_COUNT);

				for (int currentDataIndex = 0; currentDataIndex < threeDaysForecast.size(); currentDataIndex++) {

					final WeatherData currentData = threeDaysForecast.get(currentDataIndex);

					temperatureList.add(currentData.getMainData().getTemp() - 273.15);
					rainfallList.add(currentData.getCloudData().getCloud());
					pressureList.add(currentData.getMainData().getPressure());
					humidityList.add(currentData.getMainData().getHumidity());

					if (currentDataIndex != 0 && currentDataIndex % BotUtils.DAILY_WEATHER_GATHER_COUNT == 0) {
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
}
