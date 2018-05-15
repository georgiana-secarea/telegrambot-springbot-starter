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
import com.weather.telegram_chatbot_starter.handler.ProcessUpdatesHandler;

import net.aksingh.owmjapis.core.OWM;

/**
 * This class configures the Beans list for the project
 * 
 * @author stan4
 *
 */
@Configuration
@ComponentScan({ "com.weather.telegram_chatbot_starter" })
public class BeanConfig {

	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();

	@Autowired
	private Properties properties;

	@Autowired
	private ProcessUpdatesHandler updateHandler;

	@Bean
	public CommandLineRunner runTelegramBot() {
		return (args) -> {
			TelegramBot bot = getTelegramBot();
			bot.setUpdatesListener(updateHandler);
		};
	}

	/**
	 * This method generates a new TelegramBot instance
	 * 
	 * @return
	 */
	@Bean
	public TelegramBot getTelegramBot() {
		final TelegramBot bot = new TelegramBot(properties.getBotApiKey());
		return bot;
	}

	/**
	 * This method generates a new OpenWeatherMap API instance
	 * 
	 * @return
	 */
	@Bean
	public OWM getWeatherAPI() {
		final OWM owm = new OWM(properties.getWeatherApiKey());
		return owm;
	}

	/**
	 * This method generates a new GoogleGeocoding API instance
	 * 
	 * @return
	 */
	@Bean
	public GeoApiContext getGeoContext() {
		final GeoApiContext context = new GeoApiContext.Builder().apiKey(properties.getGeocodingApiKey()).build();
		return context;
	}

}
