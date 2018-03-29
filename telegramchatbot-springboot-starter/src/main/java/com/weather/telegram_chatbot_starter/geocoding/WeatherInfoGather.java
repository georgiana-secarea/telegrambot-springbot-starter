package com.weather.telegram_chatbot_starter.geocoding;

import com.weather.telegram_chatbot_starter.model.Weather;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

public class WeatherInfoGather {

	public Weather getWeather(String city) throws APIException {

		// declaring object of "OWM" class
		OWM owm = new OWM("4c7c11442dbedc998cbed8084dd3e535");

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
}
