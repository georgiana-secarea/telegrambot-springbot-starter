package com.weather.telegram_chatbot_starter.service;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

@Service
public class AnotherLocationWeatherButtonAction implements MessageCommandAction<Void> {

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final SendMessage response = new SendMessage(chatId,
				"Please choose your location first, by using the format /loc yourLocation").parseMode(ParseMode.HTML)
						.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
		bot.execute(response);

		return null;
	}

}
