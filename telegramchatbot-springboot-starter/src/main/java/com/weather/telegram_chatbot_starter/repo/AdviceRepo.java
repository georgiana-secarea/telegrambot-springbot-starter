package com.weather.telegram_chatbot_starter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.weather.telegram_chatbot_starter.model.Advice;

public interface AdviceRepo extends JpaRepository<Advice, String> {

	Advice findByCondition(String condition);

}
