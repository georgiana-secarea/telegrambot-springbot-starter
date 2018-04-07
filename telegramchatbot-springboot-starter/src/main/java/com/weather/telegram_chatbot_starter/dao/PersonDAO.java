package com.weather.telegram_chatbot_starter.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;

public class PersonDAO implements IPersonDAO{
	
	//List<Person> persons;
	
	@Autowired
	PersonRepo personRepo;
	
	public PersonDAO(PersonRepo personRepo) {
		this.personRepo = personRepo;
	}
	@Override
	public List<Person> getAllPersons() {
		
		return personRepo.findAll();
	}

	@Override
	public Person getPerson(int rollNo) {
		//Optional<Person> person = personRepo.findById(rollNo);
		return null;//person.get();
	}

	@Override
	public void updatePerson(Person person) {
		//recognized by id - it automatically updates if the id already exists
		personRepo.save(person);
		
	}

	@Override
	public void deletePerson(Person person) {
		personRepo.delete(person);
	}
	
	@Override
	public void insertPerson(Person person) {
		personRepo.save(person);
	}

}
