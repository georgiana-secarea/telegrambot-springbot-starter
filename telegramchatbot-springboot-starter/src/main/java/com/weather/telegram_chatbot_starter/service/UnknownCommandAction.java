package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IMessageDAO;
import com.weather.telegram_chatbot_starter.utils.MenuUtils;

@Service
public class UnknownCommandAction implements MessageCommandAction<Void> {
	
	@Autowired
	private IMessageDAO messageDAO;
	
	@Autowired
	private MenuUtils menuUtils;
	
	@Override
	public Void execute(TelegramBot bot, Message message) {
		Integer chatId = message.from().id();
		Integer messageId = message.messageId();
		
		final SendMessage botResponse = new SendMessage(chatId, String.format(messageDAO.getMessage("unknown")))
				.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
				.replyMarkup(menuUtils.showMainMenu());
		
		bot.execute(botResponse);
		
		return null;
	}
	
}
