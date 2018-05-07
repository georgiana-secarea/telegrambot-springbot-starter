package com.weather.telegram_chatbot_starter.service;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

@Service
public class UnknownCommandAction implements MessageCommandAction<Void> {

	@Override
	public Void execute(TelegramBot bot, Message message) {
		Integer chatId = message.from().id();
		Integer messageId = message.messageId();

		final SendMessage response = new SendMessage(chatId, "How may I be at your service?").parseMode(ParseMode.HTML)
				.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
		bot.execute(response);

		return null;
	}

}
