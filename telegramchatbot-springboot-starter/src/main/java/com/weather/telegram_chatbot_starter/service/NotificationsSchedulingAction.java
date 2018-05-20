package com.weather.telegram_chatbot_starter.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.TimeZoneApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.weather.telegram_chatbot_starter.dao.IPersonDAO;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.weather.WeatherProcessing;

@Service
public class NotificationsSchedulingAction {
	
	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	TelegramBot bot;
	
	@Autowired
	private IPersonDAO personDAO;
	
	@Autowired
	private WeatherProcessing weatherService;
	
	@Autowired
	private GeoApiContext context;
	
	@Scheduled(cron = "0 0 * * * *")
	public void pushUsersNotifications() throws NumberFormatException, ApiException, InterruptedException, IOException {
		
		final List<Person> usersList = personDAO.getAllPersons();
		
		for (Person currentUser : usersList) {
			if (Integer.parseInt(currentUser.getNotificationHour()) == getLocationCurrentHour(
					currentUser.getFavoriteCity().getName())) {
				final SendMessage botResponse = weatherService.processWeather((Integer) currentUser.getId(),
						(Integer) Integer.MAX_VALUE, currentUser.getFavoriteCity().getName(), false);
				
				bot.execute(botResponse);
			} else {
				LOGGER.debug("No users found this hour (" + LocalDateTime.now().getHour() + ") !");
			}
		}
	}
	
	private int getLocationCurrentHour(final String location) throws ApiException, InterruptedException, IOException {
		
		final GeocodingResult[] results = GeocodingApi.newRequest(context).address(location).language("en")
				.resultType(AddressType.COUNTRY, AddressType.ADMINISTRATIVE_AREA_LEVEL_2).await();
		final LatLng locationCoordinates = results[0].geometry.location;
		
		final String locationTimeZone = TimeZoneApi.getTimeZone(context, locationCoordinates).await().getID();
		final ZoneId zoneId = ZoneId.of(locationTimeZone);
		final int currentHour = ZonedDateTime.now(zoneId).getHour();
		
		return currentHour;
	}
	
}
