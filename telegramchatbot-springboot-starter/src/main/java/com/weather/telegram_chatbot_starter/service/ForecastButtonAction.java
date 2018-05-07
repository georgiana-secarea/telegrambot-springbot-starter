package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
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
		bot.execute(response);

		return null;
	}

}
