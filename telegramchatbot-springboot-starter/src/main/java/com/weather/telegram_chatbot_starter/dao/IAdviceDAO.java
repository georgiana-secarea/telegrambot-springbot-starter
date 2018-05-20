package com.weather.telegram_chatbot_starter.dao;

import com.weather.telegram_chatbot_starter.model.Advice;

public interface IAdviceDAO {
	
	public Advice getAdvice(String condition);
	
}
