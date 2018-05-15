package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.PersonDAO;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.weather.WeatherProcessing;

@Service
public class ForecastButtonAction implements MessageCommandAction<Void> {
	
	@Autowired
	private WeatherProcessing weatherService;
	
	@Autowired
	private PersonDAO personDAO;
	
	@Override
	public Void execute(TelegramBot bot, Message message) {
		
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();
		
		final Person currentPerson = personDAO.getPerson(chatId);
		
		final SendMessage botResponse = weatherService.processForecast(chatId, messageId,
				currentPerson.getLastSearchedCity());
		
		bot.execute(botResponse);
		
		return null;
	}
	
}
