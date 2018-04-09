package com.weather.telegram_chatbot_starter.geocoding;

import java.util.ArrayList;
import java.util.List;

import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.model.Weather;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;

public class WeatherInfoGather {

	// declaring object of "OWM" class
	OWM owm = new OWM("e3e4e932f1805e307be401fdbedf21a3");

	public Weather getCurrentWeather(String city) throws APIException {

		// getting current weather data for the "London" city
		CurrentWeather cwd = owm.currentWeatherByCityName(city);

		// checking data retrieval was successful or not
		if (cwd.hasRespCode() && cwd.getRespCode() == 200) {

			// checking if city name is available
			if (cwd.hasCityName()) {

				Weather currentWeather = new Weather();

				currentWeather.setTemperature(cwd.getMainData().getTemp() - 273.15);
				currentWeather.setDescription(cwd.getWeatherList().get(0).getDescription());
				currentWeather.setPressure(cwd.getMainData().getPressure());
				currentWeather.setHumidity(cwd.getMainData().getHumidity());
				currentWeather.setRainfall(cwd.getCloudData().getCloud());

				return currentWeather;
			}

		}
		return null;
	}

	public List<Forecast> getForecast(String city) throws APIException {

		// getting current weather data for the "London" city
		HourlyWeatherForecast forecast = owm.hourlyWeatherForecastByCityName(city);

		// checking data retrieval was successful or not
		if (forecast.hasRespCode() && forecast.getRespCode().equals("200")) {
			// checking if city name is available
			if (forecast.hasDataList()) {

				List<Forecast> forecastList = new ArrayList<Forecast>();

				for (int currentDataIndex = 0; currentDataIndex < forecast.getDataList().size()
						/ 2; currentDataIndex += 2) {
					WeatherData currentData = forecast.getDataList().get(currentDataIndex);

					Forecast currentForecast = new Forecast();

					currentForecast.setTime(currentData.getDateTimeText());
					currentForecast.setTemperature(currentData.getMainData().getTemp() - 273.15);
					currentForecast.setRainfall(currentData.getCloudData().getCloud());
					currentForecast.setPressure(currentData.getMainData().getPressure());
					currentForecast.setHumidity(currentData.getMainData().getHumidity());
					currentForecast.setDescription(currentData.getWeatherList().get(0).getDescription());

					forecastList.add(currentForecast);
				}

				return forecastList;
			}
		}
		return null;
	}

}
