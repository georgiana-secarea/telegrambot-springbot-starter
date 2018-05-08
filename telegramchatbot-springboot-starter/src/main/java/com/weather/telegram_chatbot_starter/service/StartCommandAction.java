package com.weather.telegram_chatbot_starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;
<<<<<<< HEAD
import com.weather.telegram_chatbot_starter.utils.UItils;

@Service
public class StartCommandAction implements MessageCommandAction<Void> {

	@Autowired
	private PersonRepo personRepo;

	@Autowired
	private IPersonDAO personDAO;

	@Override
	public Void execute(TelegramBot bot, Message message) {
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final Person person = personRepo.findById(chatId);

		if (person != null && person.getFirstName() != null) {

			final SendMessage response = new SendMessage(chatId,
					"Welcome back, " + person.getFirstName() + " " + person.getLastName()
							+ ". What would you wish to check?").parseMode(ParseMode.HTML).disableNotification(false)
									.replyToMessageId(messageId).replyMarkup(UItils.showMainMenu());
			bot.execute(response);
		} else {
			personDAO.insertPerson(chatId);

			final SendMessage response = UItils.shareDetailsMenu(chatId, messageId);
=======
import com.weather.telegram_chatbot_starter.utils.BotUtils;

@Service
public class StartCommandAction implements MessageCommandAction<Void> {

	@Autowired
	private PersonRepo personRepo;

	@Autowired
	private IPersonDAO personDAO;

	@Override
	public Void execute(TelegramBot bot, Message message) {
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();

		final Person person = personRepo.findById(chatId);

		if (person != null && person.getFirstName() != null) {

			final SendMessage response = new SendMessage(chatId,
					"Welcome back, " + person.getFirstName() + " " + person.getLastName()
							+ ". What would you wish to check?").parseMode(ParseMode.HTML).disableNotification(false)
									.replyToMessageId(messageId).replyMarkup(BotUtils.showMainMenu());
			bot.execute(response);
		} else {
			personDAO.insertPerson(chatId);

			final SendMessage response = BotUtils.shareDetailsMenu(chatId, messageId);
>>>>>>> branch 'master' of https://github.com/georgiana-secarea/telegrambot-springbot-starter
			bot.execute(response);
		}
		return null;
	}
}
