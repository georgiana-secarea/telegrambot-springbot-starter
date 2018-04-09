package com.weather.telegram_chatbot_starter.model;

public class Forecast {

	public String time;
	public Double temperature;
	public String description;
	public Double pressure;
	public Double humidity;
	public Double rainfall;

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
		return "Time: " + time + " | Temperature: " + String.format("%.2f", temperature) + "°C | Description: "
				+ description + " | Pressure: " + pressure + " HPA | Humidity: " + humidity + "% | Rainfall: "
				+ rainfall + "% | \n\n";
	}

}
