package com.weather.telegram_chatbot_starter.service;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
<<<<<<< HEAD
import com.weather.telegram_chatbot_starter.utils.UItils;

@Service
public class BackToMenuButtonAction implements MessageCommandAction<Void> {

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final SendMessage response = new SendMessage(chatId, "Below you can find the main menu.")
				.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
				.replyMarkup(UItils.showMainMenu());
=======
import com.weather.telegram_chatbot_starter.utils.BotUtils;

@Service
public class BackToMenuButtonAction implements MessageCommandAction<Void> {

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final SendMessage response = new SendMessage(chatId, "Below you can find the main menu.")
				.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
				.replyMarkup(BotUtils.showMainMenu());
>>>>>>> branch 'master' of https://github.com/georgiana-secarea/telegrambot-springbot-starter
		bot.execute(response);

		return null;
	}

}