package com.weather.telegram_chatbot_starter.service;

import com.weather.telegram_chatbot_starter.dto.BasicInfoDto;

public interface BasicService {

	BasicInfoDto addNewOrGetExisting(String name);
}
