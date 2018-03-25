package com.weather.telegram_chatbot_starter.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.weather.telegram_chatbot_starter.dto.BasicInfoDto;
import com.weather.telegram_chatbot_starter.dto.PersonDto;
import com.weather.telegram_chatbot_starter.model.BasicInfo;
import com.weather.telegram_chatbot_starter.model.Person;
import com.weather.telegram_chatbot_starter.repo.PersonRepo;
import com.weather.telegram_chatbot_starter.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService{

	 private static final Logger LOGGER = LogManager.getLogger();
	 private PersonRepo personRepo;
	 
	 @Autowired
	 public PersonServiceImpl(PersonRepo personRepo) {
		 this.personRepo = personRepo;
	 }
		@Override
		@Transactional
		public PersonDto addNewOrGetExisting(int id) {
			Optional<Person> person = personRepo.findById(id);
			if (person.isPresent()) {
				LOGGER.info(() -> String.format("The person with id %s is allready present",id));
				return toPersonDto(person.get());
			} else {
				LOGGER.info(() -> String.format("The person with id %s is not present",id));
				Person person2 = new Person();
				person2.setUserId(id);
				try {
					personRepo.save(person2);
					return toPersonDto(person2);
				} catch (DataIntegrityViolationException ex) {
					throw new IllegalArgumentException(
							String.format("The name %s already exists in this seesion, please try with other name.", id));
				}

			}
		}

		private PersonDto toPersonDto(Person basic) {
			return new PersonDto(basic.getUserId());
		}
}
