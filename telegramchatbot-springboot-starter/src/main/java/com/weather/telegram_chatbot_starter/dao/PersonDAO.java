package com.weather.telegram_chatbot_starter.dao;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	final PersonRepo personRepo;

	final LocationRepo locationRepo;

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
		final Person person = personRepo.findById(rollNo);
		return person;
	}

	@Override
	public void insertPerson(Contact contact) {
		final Person person = new Person();
		person.setUserId(contact.userId());
		person.setPhoneNumber(contact.phoneNumber());
		person.setFirstName(contact.firstName());
		person.setLastName(contact.lastName());
		personRepo.save(person);
	}

	@Override
	public void insertPerson(int chatId) {
		final Person person = new Person();
		person.setUserId(chatId);
		personRepo.save(person);
	}

	@Override
	@Transactional
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
	public void insertNotificationHour(String hour, int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null) {
			LOGGER.info("Trying to add notification hour...");
			person.setNotificationHour(hour);
			personRepo.save(person);
			LOGGER.info("Notification hour has been saved!");
		}
	}

	@Override
	@Transactional
	public void insertTimeZone(String timeZone, int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null) {
			LOGGER.info("Trying to add user timezone...");
			person.setTimeZoneCET(timeZone);
			personRepo.save(person);
			LOGGER.info("User timezone has been saved!");
		}
	}

	@Override
	public String getFavoriteLocationForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && person.getFavoriteCity() != null) {
			return person.getFavoriteCity().getName();
		}
		return null;
	}

	@Override
	public String getLastSearchedLocationForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && person.getLastSearchedCity() != null) {
			return person.getLastSearchedCity();
		}
		return null;
	}

	@Override
	public String getNotificationHourForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && person.getNotificationHour() != null) {
			return person.getNotificationHour();
		}
		return null;
	}

	@Override
	public String getTimeZoneForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && person.getTimeZoneCET() != null) {
			return person.getTimeZoneCET();
		}
		return null;
	}

	@Override
	public Set<Location> getHistoryForUser(int userId) {
		final Person person = personRepo.findById(userId);
		if (person != null && !person.getCity().isEmpty()) {
			return person.getCity();
		}
		return null;
	}

}
