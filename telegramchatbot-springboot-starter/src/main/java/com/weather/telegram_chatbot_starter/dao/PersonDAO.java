package com.weather.telegram_chatbot_starter.dao;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pengrad.telegrambot.model.Contact;
import com.weather.telegram_chatbot_starter.model.City;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.repo.LocationRepo;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;

@Repository
public class PersonDAO implements IPersonDAO {

	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();

	PersonRepo personRepo;

	LocationRepo locationRepo;

	public PersonDAO(PersonRepo personRepo, LocationRepo locationRepo) {
		this.personRepo = personRepo;
		this.locationRepo = locationRepo;
	}

	@Override
	public List<Person> getAllPersons() {

		return personRepo.findAll();
	}

	@Override
	public Person getPerson(int rollNo) {
		Person person = personRepo.findById(rollNo);
		return person;
	}

	@Override
	public void insertPerson(Contact contact) {

		Person person = new Person();
		person.setUserId(contact.userId());
		person.setPhoneNumber(contact.phoneNumber());
		person.setFirstName(contact.firstName());
		person.setLastName(contact.lastName());

		personRepo.save(person);
	}

	@Override
	public void insertPerson(int chatId) {

		Person person = new Person();
		person.setUserId(chatId);

		personRepo.save(person);
	}

	@Override
	@Transactional
	public void insertLocation(String location, int userId) {

		Person person = personRepo.findById(userId);
		if (person != null) {
			City city = new City();
			LOGGER.info("--------------------Trying to add city");
			city = locationRepo.findByName(location);

			// if city is in the database
			if (city != null) {
				LOGGER.info("--------------City is not null " + city.getId());

				person.getCity().add(city);

				personRepo.save(person);

			}
			// if city is NOT in the database
			else {
				LOGGER.info("--------------City wasn't in the database ");
				City city2 = new City();
				city2.setName(location);
				locationRepo.save(city2);
				city2 = locationRepo.findByName(location);
				LOGGER.info("--------------City added to db:  " + city2.getId());
				person.getCity().add(city2);

				personRepo.save(person);
			}

		}

	}

	@Override
	@Transactional
	public void insertFavoriteLocation(String location, int userId) {
		Person person = personRepo.findById(userId);
		if (person != null) {
			City city = new City();
			city.setName(location);

			LOGGER.info("Trying to add favorite city");
			city = locationRepo.findByName(location);
			// if city is in the database
			if (city != null) {
				person.setFavoriteCity(city);
				LOGGER.info("City is not null " + city.getId());
				LOGGER.info("Saving favorite city to person ");
				personRepo.save(person);
				LOGGER.info("Saved");

			}
			// if city is NOT in the database
			else {
				City city2 = new City();
				city2.setName(location);
				locationRepo.save(city2);
				city2 = locationRepo.findByName(location);
				LOGGER.info("City wasn't in the database " + city2.getId());

				person.setFavoriteCity(city2);
				personRepo.save(person);
			}

		}

	}

	@Override
	public String getFavoriteLocationForUser(int userId) {

		Person person = personRepo.findById(userId);
		if (person != null && person.getFavoriteCity() != null) {
			return person.getFavoriteCity().getName();
		}
		return null;
	}

	@Override
	public Set<City> getHistoryForUser(int userId) {

		Person person = personRepo.findById(userId);
		if (person != null && !person.getCity().isEmpty()) {
			return person.getCity();
		}
		return null;
	}

}
