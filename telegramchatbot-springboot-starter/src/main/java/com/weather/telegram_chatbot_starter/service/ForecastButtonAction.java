package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
<<<<<<< HEAD
import com.weather.telegram_chatbot_starter.utils.UItils;
import com.weather.telegram_chatbot_starter.weather.WeatherProcessing;

@Service
public class ForecastButtonAction implements MessageCommandAction<Void> {

	@Autowired
	private WeatherProcessing weatherService;

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final SendMessage response = weatherService.processForecast(chatId, messageId, UItils.locationStr);
=======
import com.weather.telegram_chatbot_starter.handler.HandlerUtils;
import com.weather.telegram_chatbot_starter.utils.BotUtils;

@Service
public class ForecastButtonAction implements MessageCommandAction<Void> {

	@Autowired
	private HandlerUtils handlerUtils;

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final SendMessage response = handlerUtils.processForecast(chatId, messageId, BotUtils.locationStr);
>>>>>>> branch 'master' of https://github.com/georgiana-secarea/telegrambot-springbot-starter
		bot.execute(response);

		return null;
	}

}
