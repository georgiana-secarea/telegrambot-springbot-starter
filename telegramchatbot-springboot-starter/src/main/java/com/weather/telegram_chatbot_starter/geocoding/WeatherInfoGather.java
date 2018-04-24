package com.weather.telegram_chatbot_starter.geocoding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.weather.telegram_chatbot_starter.model.Forecast;
import com.weather.telegram_chatbot_starter.model.Weather;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
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

				HashMap<String, String> adviceMap = getWeatherAdvice();

				currentWeather.setTemperature(cwd.getMainData().getTemp() - 273.15);
				currentWeather.setDescription(cwd.getWeatherList().get(0).getDescription());
				currentWeather.setPressure(cwd.getMainData().getPressure());
				currentWeather.setHumidity(cwd.getMainData().getHumidity());
				currentWeather.setRainfall(cwd.getCloudData().getCloud());
				currentWeather.setAdvice(adviceMap.get(cwd.getWeatherList().get(0).getMainInfo()));
				return currentWeather;
			}

		}
		return null;
	}

	public List<Forecast> getForecast(String city) throws APIException {

		// getting current weather data for the desired city
		HourlyWeatherForecast forecast = owm.hourlyWeatherForecastByCityName(city);

		// checking data retrieval was successful or not
		if (forecast.hasRespCode() && forecast.getRespCode().equals("200")) {
			// checking if city name is available
			if (forecast.hasDataList()) {

				List<Forecast> forecastList = new ArrayList<Forecast>();
				List<Double> temperatureList = new ArrayList<Double>();
				List<Double> rainfallList = new ArrayList<Double>();
				List<Double> pressureList = new ArrayList<Double>();
				List<Double> humidityList = new ArrayList<Double>();

				for (int currentDataIndex = 0; currentDataIndex < forecast.getDataList().size(); currentDataIndex++) {
					WeatherData currentData = forecast.getDataList().get(currentDataIndex);

					temperatureList.add(currentData.getMainData().getTemp() - 273.15);
					rainfallList.add(currentData.getCloudData().getCloud());
					pressureList.add(currentData.getMainData().getPressure());
					humidityList.add(currentData.getMainData().getHumidity());

					if (currentDataIndex != 0 && currentDataIndex % 8 == 0) {
						Double minTemp = temperatureList.stream().mapToDouble(val -> val).min().getAsDouble();
						Double avgTemp = temperatureList.stream().mapToDouble(val -> val).average().getAsDouble();
						Double maxTemp = temperatureList.stream().mapToDouble(val -> val).max().getAsDouble();
						Double avgRainfall = rainfallList.stream().mapToDouble(val -> val).average().getAsDouble();
						Double avgPressure = pressureList.stream().mapToDouble(val -> val).average().getAsDouble();
						Double avgHumidity = humidityList.stream().mapToDouble(val -> val).average().getAsDouble();

						Forecast currentForecast = new Forecast();
						currentForecast.setDescription(currentData.getWeatherList().get(0).getDescription());
						currentForecast.setMinTemp(minTemp);
						currentForecast.setAvgTemp(avgTemp);
						currentForecast.setMaxTemp(maxTemp);
						currentForecast.setRainfall(avgRainfall);
						currentForecast.setPressure(avgPressure);
						currentForecast.setHumidity(avgHumidity);

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

	private HashMap<String, String> getWeatherAdvice() {
		HashMap<String, String> adviceMap = new HashMap<>();
		adviceMap.put("Clear", "It's time for a picnic. A clear blue sky is waiting for you.");
		adviceMap.put("Few clouds", "The sun is hiding from you momentarily, yet life can still be enjoyable.");
		adviceMap.put("Rain", "Don't forget the shower gel, as you're going to take a bath today.");
		adviceMap.put("Thunderstorm",
				"Thunderstruck! Oh, sorry, too much AC/DC ruined my creator's mind. In a positive way, that is. Nevermind, I can report a thunderstom as we ... speak");
		adviceMap.put("Snow", "The snow is snowing, enjoy the snowflakes!");
		return adviceMap;
	}

}
