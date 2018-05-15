package com.weather.telegram_chatbot_starter.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.weather.telegram_chatbot_starter.model.Person;

/**
 * This is the JpaRepository based repo for the Person table objects
 * 
 * @author stan4
 *
 */
public interface PersonRepo extends JpaRepository<Person, Long> {

	Person findById(int id);

	Optional<Person> findByPhoneNumber(String phone);
}
