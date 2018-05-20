package com.weather.telegram_chatbot_starter.dao;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.pengrad.telegrambot.model.Contact;
import com.weather.telegram_chatbot_starter.model.*;

public interface IPersonDAO {
	
	public List<Person> getAllPersons();
	
	public Person getPerson(int userId);
	
	public void insertPerson(Contact contact);
	
	public void insertPerson(int userId);
	
	@Transactional
	public void insertLocation(String location, int userId);
	
	@Transactional
	public void insertLastSearchedLocation(String location, int userId);
	
	@Transactional
	public void insertFavoriteLocation(String location, int userId);
	
	@Transactional
	public void insertNotificationHour(String hour, int userId);
	
	public String getFavoriteLocationForUser(int userId);
	
	public String getLastSearchedLocationForUser(int userId);
	
	public String getNotificationHourForUser(int userId);
	
	public Set<Location> getHistoryForUser(int userId);
	
}
