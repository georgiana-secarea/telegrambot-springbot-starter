package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IMessageDAO;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;

@Service
public class NotificationHourCommandAction implements MessageCommandAction<Void> {
	
	@Autowired
	private IPersonDAO personDAO;
	
	@Autowired
	private IMessageDAO messageDAO;
	
	@Override
	public Void execute(TelegramBot bot, Message message) {
		
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();
		final String messageText = message.text();
		
		SendMessage botResponse;
		
		final String[] inputNotificationHour = messageText.split("/hour ", 2);
		final boolean isNumeric = inputNotificationHour[1].chars().allMatch(Character::isDigit);
		if (isNumeric) {
			final int hour = Integer.parseInt(inputNotificationHour[1]);
			if (hour >= 0 && hour <= 59) {
				personDAO.insertNotificationHour(inputNotificationHour[1], chatId);
				
				botResponse = new SendMessage(chatId,
						String.format(messageDAO.getMessage("savedNotificationHour"), inputNotificationHour[1]))
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(new ForceReply());
			} else {
				botResponse = new SendMessage(chatId,
						String.format(messageDAO.getMessage("invalidNotificationHourValue"), inputNotificationHour[1]))
								.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
								.replyMarkup(new ForceReply());
			}
		} else {
			botResponse = new SendMessage(chatId,
					String.format(messageDAO.getMessage("invalidNotificationHourFormat"), inputNotificationHour[1]))
							.parseMode(ParseMode.HTML).disableNotification(false).replyToMessageId(messageId)
							.replyMarkup(new ForceReply());
		}
		bot.execute(botResponse);
		return null;
	}
}
