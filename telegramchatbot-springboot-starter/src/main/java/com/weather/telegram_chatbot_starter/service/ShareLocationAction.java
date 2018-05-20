package com.weather.telegram_chatbot_starter.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IMessageDAO;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.weather.WeatherProcessing;

@Service
public class ShareLocationAction implements MessageCommandAction<Void> {
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private IPersonDAO personDAO;
	
	@Autowired
	private GeoApiContext context;
	
	@Autowired
	private WeatherProcessing weatherService;
	
	@Autowired
	private IMessageDAO messageDAO;
	
	@Override
	public Void execute(TelegramBot bot, Message message) {
		
		final Integer chatId = message.from().id();
		final Integer messageId = message.messageId();
		final Location userLocation = message.location();
		
		String currentLocation = "N/A";
		try {
			currentLocation = getCity(userLocation.latitude(), userLocation.longitude());
		} catch (ApiException | InterruptedException | IOException e) {
			LOGGER.error(String.format("Error caught while trying to parse the location: " + e.getMessage()));
			
			final SendMessage botResponse = new SendMessage(chatId,
					String.format(messageDAO.getMessage("conversionException"))).parseMode(ParseMode.HTML)
							.disableNotification(false).replyToMessageId(messageId).replyMarkup(new ForceReply());
			bot.execute(botResponse);
			
			return null;
		}
		
		personDAO.insertLocation(currentLocation, chatId);
		
		final SendMessage response = weatherService.processWeather(chatId, messageId, currentLocation, false);
		bot.execute(response);
		
		return null;
	}
	
	private String getCity(double lat, double lng) throws ApiException, InterruptedException, IOException {
		
		final GeocodingResult[] results = GeocodingApi.newRequest(context).latlng(new LatLng(lat, lng)).language("en")
				.resultType(AddressType.COUNTRY, AddressType.ADMINISTRATIVE_AREA_LEVEL_2).await();
		
		final String city = results[0].addressComponents[0].longName;
		
		return city;
		
	}
}
