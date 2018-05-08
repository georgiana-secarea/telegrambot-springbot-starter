package com.weather.telegram_chatbot_starter.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.maps.GeoApiContext;
import com.pengrad.telegrambot.TelegramBot;
import com.weather.telegram_chatbot_starter.handler.SimpleUpdateHandler;
<<<<<<< HEAD
import com.weather.telegram_chatbot_starter.utils.UItils;
=======
import com.weather.telegram_chatbot_starter.utils.BotUtils;
>>>>>>> branch 'master' of https://github.com/georgiana-secarea/telegrambot-springbot-starter

import net.aksingh.owmjapis.core.OWM;

@Configuration
@ComponentScan({ "com.weather.telegram_chatbot_starter" })
public class BeanConfig {

	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();

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
<<<<<<< HEAD
		final TelegramBot bot = new TelegramBot(UItils.BOT_API_TOKEN);
=======
		final TelegramBot bot = new TelegramBot(BotUtils.BOT_API_TOKEN);
>>>>>>> branch 'master' of https://github.com/georgiana-secarea/telegrambot-springbot-starter
		return bot;
	}

	@Bean
	public OWM getWeatherAPI() {
<<<<<<< HEAD
		final OWM owm = new OWM(UItils.WEATHER_API_TOKEN);
=======
		final OWM owm = new OWM(BotUtils.WEATHER_API_TOKEN);
>>>>>>> branch 'master' of https://github.com/georgiana-secarea/telegrambot-springbot-starter
		return owm;
	}

	@Bean
	public GeoApiContext getGeoContext() {
<<<<<<< HEAD
		final GeoApiContext context = new GeoApiContext.Builder().apiKey(UItils.GEOCODING_API_TOKEN).build();
=======
		final GeoApiContext context = new GeoApiContext.Builder().apiKey(BotUtils.GEOCODING_API_TOKEN).build();
>>>>>>> branch 'master' of https://github.com/georgiana-secarea/telegrambot-springbot-starter
		return context;
	}

}
