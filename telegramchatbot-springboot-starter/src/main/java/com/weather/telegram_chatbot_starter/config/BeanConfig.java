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
import com.weather.telegram_chatbot_starter.utils.BotUtils;

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
		final TelegramBot bot = new TelegramBot(BotUtils.BOT_API_TOKEN);
		return bot;
	}

	@Bean
	public OWM getWeatherAPI() {
		final OWM owm = new OWM(BotUtils.WEATHER_API_TOKEN);
		return owm;
	}

	@Bean
	public GeoApiContext getGeoContext() {
		final GeoApiContext context = new GeoApiContext.Builder().apiKey(BotUtils.GEOCODING_API_TOKEN).build();
		return context;
	}

}
