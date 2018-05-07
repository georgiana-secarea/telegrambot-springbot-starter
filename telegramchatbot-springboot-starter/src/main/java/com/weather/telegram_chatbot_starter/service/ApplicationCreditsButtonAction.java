package com.weather.telegram_chatbot_starter.service;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.utils.UItils;

@Service
public class ApplicationCreditsButtonAction implements MessageCommandAction<Void> {

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final SendMessage response = new SendMessage(chatId,
				"Developers: Georgiana Secarea and Mircea Stan;\n"
						+ "Special thanks to Vlad for his continuous support throughout this project!")
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(UItils.showMainMenu());
		bot.execute(response);

		return null;
	}

}
