package com.weather.telegram_chatbot_starter.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.weather.WeatherProcessing;

@Service
public class SearchHistoryButtonAction implements MessageCommandAction<Void> {

	@Autowired
	private IPersonDAO personDAO;

	@Autowired
	private WeatherProcessing weatherService;

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final Set<City> cities = personDAO.getHistoryForUser(chatId);

		final SendMessage response = weatherService.retrieveUserSearchHistory(chatId, messageId, cities);

		bot.execute(response);

		return null;
	}

}
