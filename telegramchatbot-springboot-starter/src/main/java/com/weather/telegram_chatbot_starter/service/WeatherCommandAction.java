package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.weather.WeatherProcessing;

@Service
public class WeatherCommandAction implements MessageCommandAction<Void> {
	
	@Autowired
	private IPersonDAO personDAO;
	
	@Autowired
	private WeatherProcessing weatherService;
	
	@Override
	public Void execute(TelegramBot bot, Message message) {
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();
		final String messageText = message.text();
		
		final String[] inputLocation = messageText.split("/loc ", 2);
		
		personDAO.insertLocation(inputLocation[1], chatId);
		personDAO.insertLastSearchedLocation(inputLocation[1], chatId);
		
		final SendMessage botResponse = weatherService.processWeather(chatId, messageId, inputLocation[1], false);
		
		bot.execute(botResponse);
		
		return null;
	}
	
}
