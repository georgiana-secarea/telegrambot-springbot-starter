package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IMessageDAO;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;

@Service
public class ShareContactAction implements MessageCommandAction<Void> {
	
	@Autowired
	private IPersonDAO personDAO;
	
	@Autowired
	private IMessageDAO messageDAO;
	
	@Override
	public Void execute(TelegramBot bot, Message message) {
		
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();
		final Contact userContact = message.contact();
		
		personDAO.insertPerson(userContact);
		
		final SendMessage botResponse = new SendMessage(chatId,
				String.format(messageDAO.getMessage("savedContactDetails"), userContact.phoneNumber()))
						.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
						.replyMarkup(new ForceReply());
		
		bot.execute(botResponse);
		
		return null;
	}
	
}
