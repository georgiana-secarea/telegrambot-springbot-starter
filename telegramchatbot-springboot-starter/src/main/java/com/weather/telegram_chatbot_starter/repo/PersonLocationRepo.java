package com.weather.telegram_chatbot_starter.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weather.telegram_chatbot_starter.model.PersonLocation;
import com.weather.telegram_chatbot_starter.model.PersonLocationId;

public interface PersonLocationRepo extends JpaRepository<PersonLocation, Long> {

	Optional<PersonLocation> findById(PersonLocationId name);

}
