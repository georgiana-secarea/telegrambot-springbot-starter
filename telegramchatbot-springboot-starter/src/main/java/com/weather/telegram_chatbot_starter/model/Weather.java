package com.weather.telegram_chatbot_starter.model;

public class Weather {

	private Double temperature;
	private String description;
	private Double pressure;
	private Double humidity;
	private Double rainfall;

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

	@Override
	public String toString() {
		return "\r\n Temperature: " + temperature + "�C \r\n Description: " + description + " \r\n Rainfall: " + rainfall + "% \r\n Pressure: " + pressure
				+ " HPA \r\n Humidity: " + humidity + "%";
	}

}