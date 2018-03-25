package com.weather.telegram_chatbot_starter.dto;

public class PersonDto {
	
	private int id ;
	
	private String phoneNumber;

	private String firstName;

	private String lastName;

	public PersonDto() {

	}

	public PersonDto(int id) {
		super();
		this.id = id;

	}
}
