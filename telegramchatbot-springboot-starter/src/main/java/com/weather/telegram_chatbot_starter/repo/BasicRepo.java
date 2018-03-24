package com.weather.telegram_chatbot_starter.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weather.telegram_chatbot_starter.model.BasicInfo;

public interface BasicRepo extends JpaRepository<BasicInfo, Long>{

	Optional<BasicInfo> findById(Long name);
	
	Optional<BasicInfo>	findByName(String name);

}
