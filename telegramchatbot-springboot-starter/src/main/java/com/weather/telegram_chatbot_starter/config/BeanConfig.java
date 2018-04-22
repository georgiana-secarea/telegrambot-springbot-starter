package com.weather.telegram_chatbot_starter.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.pengrad.telegrambot.TelegramBot;

import com.weather.telegram_chatbot_starter.handler.SimpleUpdateHandler;
import com.weather.telegram_chatbot_starter.notifications.NotificationService;

@Configuration
@ComponentScan({ "com.weather.telegram_chatbot_starter" })
public class BeanConfig {

	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();

	private static final String API_TOKEN = "582035472:AAGkvIK1LClMwmrAFgo6Ocgk_h936LINv6k";//Wearther bot
	//private static final String API_TOKEN = "570157304:AAGqgBeHGOul3zn2NcqdoICVf_XLYO45TAE";//Georcea bot
	

	@Autowired
	private SimpleUpdateHandler updateHandler;
	
	private NotificationService notificationService;

	@Bean
	public CommandLineRunner runTelegramBot() {
		return (args) -> {
			TelegramBot bot = getTelegramBot();
			this.notificationService = new NotificationService(bot);
			this.notificationService.notifyOnceADay(535468464);
			bot.setUpdatesListener(updateHandler);
		};
	}

	@Bean
	public TelegramBot getTelegramBot() {
		TelegramBot bot = new TelegramBot(API_TOKEN);
		return bot;
	}
}
