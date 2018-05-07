package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;

@Service
public class ShareContactAction implements MessageCommandAction<Void> {

	@Autowired
	private IPersonDAO personDAO;

	@Override
	public Void execute(TelegramBot bot, Message message) {

		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();
		final Contact userContact = message.contact();

		personDAO.insertPerson(userContact);

		final SendMessage response = new SendMessage(chatId,
				"Your contact details have been saved internally (" + userContact.phoneNumber()
						+ ").\nNow please enter your favorite location where you want your notifications to be received"
						+ ", using the format \"/fav yourLocation\"").parseMode(ParseMode.HTML)
								.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
		bot.execute(response);

		return null;
	}

}
