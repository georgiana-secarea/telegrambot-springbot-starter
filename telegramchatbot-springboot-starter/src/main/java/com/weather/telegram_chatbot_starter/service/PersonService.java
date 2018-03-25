package com.weather.telegram_chatbot_starter.service;

import com.weather.telegram_chatbot_starter.dto.PersonDto;

public interface PersonService {

	PersonDto addNewOrGetExisting(int id);

}
