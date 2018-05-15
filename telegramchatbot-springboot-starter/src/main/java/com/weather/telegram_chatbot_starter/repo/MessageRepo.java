package com.weather.telegram_chatbot_starter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.weather.telegram_chatbot_starter.model.Message;

/**
 * This is the JpaRepository based repo for the Advice table objects
 * 
 * @author stan4
 *
 */
public interface MessageRepo extends JpaRepository<Message, String> {
	
	Message findByMeaning(String condition);
	
}
