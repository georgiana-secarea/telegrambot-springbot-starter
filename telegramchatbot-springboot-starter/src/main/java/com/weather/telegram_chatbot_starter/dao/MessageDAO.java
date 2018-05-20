package com.weather.telegram_chatbot_starter.dao;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;
import com.weather.telegram_chatbot_starter.model.Message;
import com.weather.telegram_chatbot_starter.repo.MessageRepo;

@Repository
public class MessageDAO implements IMessageDAO {
	
	final private MessageRepo messageRepo;
	
	public MessageDAO(MessageRepo messageRepo) {
		this.messageRepo = messageRepo;
	}
	
	@Override
	@CachePut("messages")
	public String getMessage(String meaning) {
		final Message message = messageRepo.findByMeaning(meaning);
		return message.getMessage();
	}
	
}
