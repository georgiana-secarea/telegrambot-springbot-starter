package com.weather.telegram_chatbot_starter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.weather.telegram_chatbot_starter.dto.PersonDto;
import com.weather.telegram_chatbot_starter.service.PersonService;

@RestController
public class PersonController {
	
	private PersonService personService;
	
	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}
	@RequestMapping(value = "/person/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<PersonDto> getPerson(@PathVariable("id") int id) {
		return ResponseEntity.ok().body(personService.addNewOrGetExisting(id));
	}
}
