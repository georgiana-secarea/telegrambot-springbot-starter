package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IMessageDAO;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;
import com.weather.telegram_chatbot_starter.utils.MenuUtils;

@Service
public class StartCommandAction implements MessageCommandAction<Void> {
	
	@Autowired
	private PersonRepo personRepo;
	
	@Autowired
	private IPersonDAO personDAO;
	
	@Autowired
	private IMessageDAO messageDAO;
	
	@Autowired
	private MenuUtils menuUtils;
	
	@Override
	public Void execute(TelegramBot bot, Message message) {
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();
		SendMessage botResponse;
		
		final Person inputPerson = personRepo.findById(chatId);
		
		if (inputPerson != null && inputPerson.getFirstName() != null) {
			
			botResponse = new SendMessage(chatId,
					String.format(messageDAO.getMessage("startRegisteredUser"), inputPerson.getFirstName()))
							.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(menuUtils.showMainMenu());
			
			bot.execute(botResponse);
		} else {
			personDAO.insertPerson(chatId);
			
			ReplyKeyboardMarkup shareKeyboard = menuUtils.shareDetailsMenu(chatId, messageId);
			
			botResponse = new SendMessage(chatId, messageDAO.getMessage("startNewUser")).parseMode(ParseMode.HTML)
					.disableNotification(false).replyToMessageId(messageId).replyMarkup(shareKeyboard);
			
			bot.execute(botResponse);
		}
		return null;
	}
}
