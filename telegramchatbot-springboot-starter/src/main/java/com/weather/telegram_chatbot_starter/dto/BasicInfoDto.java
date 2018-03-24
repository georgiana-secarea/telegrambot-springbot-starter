package com.weather.telegram_chatbot_starter.dto;

public class BasicInfoDto {
	
	private String name;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BasicInfoDto() {
	} 
	
	public BasicInfoDto(String name) {
		super();
		this.name = name;
	} 
	
	
}
