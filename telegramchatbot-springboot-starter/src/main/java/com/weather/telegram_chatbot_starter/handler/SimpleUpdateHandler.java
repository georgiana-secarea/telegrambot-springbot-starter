package com.weather.telegram_chatbot_starter.handler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.weather.telegram_chatbot_starter.service.AnotherLocationWeatherButtonAction;
import com.weather.telegram_chatbot_starter.service.ApplicationCreditsButtonAction;
import com.weather.telegram_chatbot_starter.service.BackToMenuButtonAction;
import com.weather.telegram_chatbot_starter.service.DenyButtonAction;
import com.weather.telegram_chatbot_starter.service.FavoriteLocationCommandAction;
import com.weather.telegram_chatbot_starter.service.ForecastButtonAction;
import com.weather.telegram_chatbot_starter.service.SearchHistoryButtonAction;
import com.weather.telegram_chatbot_starter.service.ShareContactAction;
import com.weather.telegram_chatbot_starter.service.ShareLocationAction;
import com.weather.telegram_chatbot_starter.service.StartCommandAction;
import com.weather.telegram_chatbot_starter.service.UnknownCommandAction;
import com.weather.telegram_chatbot_starter.service.WeatherCommandAction;

import com.weather.telegram_chatbot_starter.utils.Utils;

@Service
public class SimpleUpdateHandler implements UpdatesListener {

	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();

	@Autowired
	private TelegramBot telegramBot;

	@Autowired
	private StartCommandAction startCommand;

	@Autowired
	private FavoriteLocationCommandAction favoriteLocationCommand;

	@Autowired
	private WeatherCommandAction chosenLocationWeatherCommand;

	@Autowired
	private ShareContactAction shareContactAction;

	@Autowired
	private ShareLocationAction shareLocationAction;

	@Autowired
	private DenyButtonAction denyButton;

	@Autowired
	private ForecastButtonAction forecastButton;

	@Autowired
	private AnotherLocationWeatherButtonAction anotherLocationWeatherButton;

	@Autowired
	private SearchHistoryButtonAction searchHistoryButton;

	@Autowired
	private BackToMenuButtonAction backToMenuButton;

	@Autowired
	private ApplicationCreditsButtonAction applicationCreditsButton;

	@Autowired
	private UnknownCommandAction unknownButton;

	@Override
	public int process(List<Update> updates) {
		for (Update update : updates) {
			process(update);
		}
		return UpdatesListener.CONFIRMED_UPDATES_ALL;
	}

	/**
	 * This method dispatches the type of received update to the correct routine
	 * 
	 * @param update
	 */

	private void process(Update update) {
		if (update.message() != null && update.message().location() != null) {
			processUserLocation(update.message());
			return;
		} else if (update.message() != null && update.message().contact() != null) {
			processUserContact(update.message());
			return;
		} else if (update.message() != null) {
			processUserMessages(update.message());
			return;
		}
	}

	/**
	 * This method processes the share location to generate the according data for
	 * the current weather
	 * 
	 * @param message
	 */
	public void processUserLocation(Message message) {
		shareLocationAction.execute(telegramBot, message);
	}

	/**
	 * This method processes the share contact to store the according data for the
	 * current user
	 * 
	 * @param message
	 */
	public void processUserContact(Message message) {
		shareContactAction.execute(telegramBot, message);
	}

	/**
	 * This method processes the button commands throughout the user interface,
	 * depending on their bound text value
	 * 
	 * @param message
	 */
	public void processUserMessages(Message message) {
		final String messageText = message.text();

		if (messageText.startsWith(Utils.FAVORITE_LOCATION)) {
			favoriteLocationCommand.execute(telegramBot, message);
		} else if (messageText.startsWith(Utils.CHOSEN_LOCATION_CURRENT_WEATHER)) {
			chosenLocationWeatherCommand.execute(telegramBot, message);
		} else {
			switch (messageText) {
			case Utils.START_COMMAND: {
				startCommand.execute(telegramBot, message);
				break;
			}
			case Utils.DENY_ACTION: {
				denyButton.execute(telegramBot, message);
				break;
			}
			case Utils.SHOW_FORECAST: {
				forecastButton.execute(telegramBot, message);
				break;
			}
			case Utils.ANOTHER_LOCATION_WEATHER_INFO: {
				anotherLocationWeatherButton.execute(telegramBot, message);
				break;
			}
			case Utils.SEARCH_USER_LIST_HISTORY: {
				searchHistoryButton.execute(telegramBot, message);
				break;
			}
			case Utils.BACK_TO_MENU: {
				backToMenuButton.execute(telegramBot, message);
				break;
			}
			case Utils.APPLICATION_CREDITS: {
				applicationCreditsButton.execute(telegramBot, message);
				break;
			}
			default: {
				unknownButton.execute(telegramBot, message);
				break;
			}
			}

		}
	}
}
