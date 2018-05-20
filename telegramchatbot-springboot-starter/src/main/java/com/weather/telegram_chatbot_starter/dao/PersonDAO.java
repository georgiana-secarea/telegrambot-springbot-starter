package com.weather.telegram_chatbot_starter.dao;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pengrad.telegrambot.model.Contact;
import com.weather.telegram_chatbot_starter.model.Location;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.repo.LocationRepo;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;

@Repository
public class PersonDAO implements IPersonDAO {
	
	/** The Constant LOGGER. */
	public static final Logger LOGGER = LogManager.getLogger();
	
	final private PersonRepo personRepo;
	
	final private LocationRepo locationRepo;
	
	public PersonDAO(PersonRepo personRepo, LocationRepo locationRepo) {
		this.personRepo = personRepo;
		this.locationRepo = locationRepo;
	}
	
	@Override
	@CachePut(value = "persons")
	public List<Person> getAllPersons() {
		return personRepo.findAll();
	}
	
	@Override
	@CachePut(value = "persons")
	public Person getPerson(int userId) {
		final Person person = personRepo.findById(userId);
		return person;
	}
	
	@Override
	@CachePut(value = "favoriteLocations")
	public String getFavoriteLocationForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && person.getFavoriteCity() != null) {
			return person.getFavoriteCity().getName();
		}
		return null;
	}
	
	@Override
	@CachePut(value = "lastSearchedLocations")
	public String getLastSearchedLocationForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && person.getLastSearchedCity() != null) {
			return person.getLastSearchedCity();
		}
		return null;
	}
	
	@Override
	@CachePut(value = "notificationsHour")
	public String getNotificationHourForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && person.getNotificationHour() != null) {
			return person.getNotificationHour();
		}
		return null;
	}
	
	@Override
	@CachePut(value = "userHistory")
	public Set<Location> getHistoryForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && !person.getCity().isEmpty()) {
			return person.getCity();
		}
		return null;
	}
	
	@Override
	@CacheEvict(value = "persons")
	public void insertPerson(Contact contact) {
		final Person person = new Person();
		person.setId(contact.userId());
		person.setPhoneNumber(contact.phoneNumber());
		person.setFirstName(contact.firstName());
		person.setLastName(contact.lastName());
		personRepo.save(person);
	}
	
	@Override
	@CacheEvict(value = "persons")
	public void insertPerson(int chatId) {
		final Person person = new Person();
		person.setId(chatId);
		personRepo.save(person);
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "locations")
	public void insertLocation(String inputLocation, int userId) {
		Person person = personRepo.findById(userId);
		if (person != null) {
			Location newLocation = new Location();
			LOGGER.info("Trying to add location...");
			newLocation = locationRepo.findByName(inputLocation);
			if (newLocation != null) {
				LOGGER.info("Location is not null: " + newLocation.getId());
				person.getCity().add(newLocation);
				personRepo.save(person);
				LOGGER.info("Location has been saved!");
			} else {
				LOGGER.info("Location was not in the database!");
				Location secondLocation = new Location();
				secondLocation.setName(inputLocation);
				locationRepo.save(secondLocation);
				secondLocation = locationRepo.findByName(inputLocation);
				LOGGER.info("Location added to the list: " + secondLocation.getId());
				person.getCity().add(secondLocation);
				personRepo.save(person);
			}
		}
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "locations")
	public void insertFavoriteLocation(String inputLocation, int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null) {
			Location newLocation = new Location();
			newLocation.setName(inputLocation);
			LOGGER.info("Trying to add favorite location...");
			newLocation = locationRepo.findByName(inputLocation);
			if (newLocation != null) {
				person.setFavoriteCity(newLocation);
				LOGGER.info("Location is not null: " + newLocation.getId());
				personRepo.save(person);
				LOGGER.info("Favorite location has been saved!");
			} else {
				Location secondLocation = new Location();
				secondLocation.setName(inputLocation);
				locationRepo.save(secondLocation);
				secondLocation = locationRepo.findByName(inputLocation);
				LOGGER.info("Location was not in the database: " + secondLocation.getId());
				person.setFavoriteCity(secondLocation);
				personRepo.save(person);
			}
		}
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "locations")
	public void insertLastSearchedLocation(String location, int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null) {
			LOGGER.info("Trying to add last searched location...");
			person.setLastSearchedCity(location);
			personRepo.save(person);
			LOGGER.info("Last searched location column has been updated!");
		}
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "notifications")
	public void insertNotificationHour(String hour, int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null) {
			LOGGER.info("Trying to add notification hour...");
			person.setNotificationHour(hour);
			personRepo.save(person);
			LOGGER.info("Notification hour has been saved!");
		}
	}
	
}
