package com.weather.telegram_chatbot_starter.model;

import com.vdurmont.emoji.EmojiParser;

/**
 * This is the class whose objects will store the current weather data received
 * from the OWM API call
 * 
 * @author stan4
 *
 */
public class Weather {
	
	private Double temperature;
	private String description;
	private Double pressure;
	private Double humidity;
	private Double rainfall;
	private String advice;
	private String statusEmoji;
	
	public Double getRainfall() {
		return rainfall;
	}
	
	public void setRainfall(Double rainfall) {
		this.rainfall = rainfall;
	}
	
	public Double getTemperature() {
		return temperature;
	}
	
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
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
	
	public String getAdvice() {
		return advice;
	}
	
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	
	public String getStatusEmoji() {
		return statusEmoji;
	}
	
	public void setStatusEmoji(String statusEmoji) {
		this.statusEmoji = statusEmoji;
	}
	
	@Override
	public String toString() {
		return EmojiParser.parseToUnicode("\r\n :thermometer: Temperature: " + String.format("%.2f", temperature)
				+ "°C \r\n :clipboard: Description: " + description + " \r\n :shower: Rainfall: " + rainfall
				+ "% \r\n :small_orange_diamond: Pressure: " + pressure
				+ " HPA \r\n :chart_with_downwards_trend: Humidity: " + humidity + "%" + " \r\n\n " + statusEmoji + " "
				+ advice);
	}
	
}
