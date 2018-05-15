package com.weather.telegram_chatbot_starter.dao;

import org.springframework.stereotype.Repository;
import com.weather.telegram_chatbot_starter.model.Advice;
import com.weather.telegram_chatbot_starter.repo.AdviceRepo;

@Repository
public class AdviceDAO implements IAdviceDAO {
	
	final AdviceRepo adviceRepo;
	
	public AdviceDAO(AdviceRepo adviceRepo) {
		this.adviceRepo = adviceRepo;
	}
	
	@Override
	public String getMessage(String condition) {
		final Advice advice = adviceRepo.findByCondition(condition);
		return advice.getMessage();
	}
	
}
