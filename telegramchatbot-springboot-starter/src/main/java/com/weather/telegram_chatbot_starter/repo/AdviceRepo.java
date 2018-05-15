package com.weather.telegram_chatbot_starter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.weather.telegram_chatbot_starter.model.Advice;

/**
 * This is the JpaRepository based repo for the Advice table objects
 * 
 * @author stan4
 *
 */
public interface AdviceRepo extends JpaRepository<Advice, String> {

	Advice findByCondition(String condition);

}
