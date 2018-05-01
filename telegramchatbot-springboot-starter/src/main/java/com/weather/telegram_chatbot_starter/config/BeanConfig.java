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

import net.aksingh.owmjapis.core.OWM;

@Configuration
@ComponentScan({ "com.weather.telegram_chatbot_starter" })
public class BeanConfig {

	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();

	private static final String BOT_API_TOKEN = "582035472:AAGkvIK1LClMwmrAFgo6Ocgk_h936LINv6k";
	private static final String WEATHER_API_TOKEN = "e3e4e932f1805e307be401fdbedf21a3";

	@Autowired
	private SimpleUpdateHandler updateHandler;

	@Bean
	public CommandLineRunner runTelegramBot() {
		return (args) -> {
			TelegramBot bot = getTelegramBot();
			bot.setUpdatesListener(updateHandler);
		};
	}

	@Bean
	public TelegramBot getTelegramBot() {
		TelegramBot bot = new TelegramBot(BOT_API_TOKEN);
		return bot;
	}

	@Bean
	public OWM getWeatherAPI() {
		OWM owm = new OWM(WEATHER_API_TOKEN);
		return owm;
	}

	@Bean
	public String getAddress() {
		String address = new String("");
		return address;
	}
}
