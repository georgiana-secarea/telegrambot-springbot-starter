package com.weather.telegram_chatbot_starter.model;

import com.vdurmont.emoji.EmojiParser;

/**
 * This is the class whose objects will store the forecast data received from
 * the OWM API call
 * 
 * @author stan4
 *
 */
public class Forecast {
	
	public String time;
	public Double minTemp;
	public Double avgTemp;
	public Double maxTemp;
	public String description;
	public Double pressure;
	public Double humidity;
	public Double rainfall;
	public String date;
	
	public Double getMinTemp() {
		return minTemp;
	}
	
	public void setMinTemp(Double minTemp) {
		this.minTemp = minTemp;
	}
	
	public Double getMaxTemp() {
		return maxTemp;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setMaxTemp(Double maxTemp) {
		this.maxTemp = maxTemp;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public Double getRainfall() {
		return rainfall;
	}
	
	public void setRainfall(Double rainfall) {
		this.rainfall = rainfall;
	}
	
	public Double getAvgTemp() {
		return avgTemp;
	}
	
	public void setAvgTemp(Double temp) {
		this.avgTemp = temp;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getPressure() {
		return pressure;
	}
	
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	
	public Double getHumidity() {
		return humidity;
	}
	
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	
	@Override
	public String toString() {
		return EmojiParser.parseToUnicode(" :date: Date: " + date + " \n\n :thermometer: Average temperature: "
				+ String.format("%.2f", avgTemp) + "°C \n :large_blue_circle: Minimum temperature: "
				+ String.format("%.2f", minTemp) + "°C \n :red_circle: Maximum temperature: "
				+ String.format("%.2f", maxTemp) + "°C \n :clipboard: Description: " + description
				+ " \n :small_orange_diamond: Pressure: " + String.format("%.2f", pressure)
				+ " HPA \n :chart_with_downwards_trend: Humidity: " + String.format("%.2f", humidity)
				+ "% \n :shower: Rainfall: " + String.format("%.2f", rainfall) + "% \n\n\n");
	}
	
}
