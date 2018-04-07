package com.weather.telegram_chatbot_starter.dao;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.pengrad.telegrambot.model.Contact;
import com.weather.telegram_chatbot_starter.model.*;

public interface IPersonDAO {

	public List<Person> getAllPersons();

	public Person getPerson(int rollNo);

	public void insertPerson(Contact contact) ;
	
	public void insertPerson(int chatId) ;
	
	@Transactional
	public void insertLocation(String location, int userId);
	@Transactional
	public void insertFavoriteLocation(String location, int userId);
	
	public String getFavoriteLocationForUser( int userId) ;
	
	public Set<City> getHistoryForUser( int userId) ;
}
