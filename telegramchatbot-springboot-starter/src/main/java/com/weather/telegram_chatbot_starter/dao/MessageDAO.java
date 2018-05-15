package com.weather.telegram_chatbot_starter.dao;

import org.springframework.stereotype.Repository;
import com.weather.telegram_chatbot_starter.model.Message;
import com.weather.telegram_chatbot_starter.repo.MessageRepo;

@Repository
public class MessageDAO implements IMessageDAO {
	
	final MessageRepo messageRepo;
	
	public MessageDAO(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}
	
	@Override
	public String getMessage(String meaning) {
		final Message message = messageRepo.findByMeaning(meaning);
		return message.getMessage();
	}
	
}
