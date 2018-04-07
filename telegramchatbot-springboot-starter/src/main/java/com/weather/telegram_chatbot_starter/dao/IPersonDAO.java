package com.weather.telegram_chatbot_starter.dao;

import java.util.List;

import com.weather.telegram_chatbot_starter.model.*;

public interface IPersonDAO {

	public List<Person> getAllPersons();

	public Person getPerson(int rollNo);

	public void updatePerson(Person person);
	
	public void insertPerson(Person person);

	public void deletePerson(Person person);
}
