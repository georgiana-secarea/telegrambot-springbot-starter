package com.weather.telegram_chatbot_starter.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weather.telegram_chatbot_starter.model.Location1;

public interface LocationRepo extends JpaRepository<Location1, Long> {

	Optional<Location1> findById(Long name);

	Optional<Location1> findByCity(String city);
	
}
