package com.weather.telegram_chatbot_starter.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.weather.telegram_chatbot_starter.model.City;

public interface LocationRepo extends JpaRepository<City, Long> {

	Optional<City> findById(Long name);

	City findByName(String name);

}
