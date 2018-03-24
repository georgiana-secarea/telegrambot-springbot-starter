package com.weather.telegram_chatbot_starter.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.weather.telegram_chatbot_starter.dto.BasicInfoDto;
import com.weather.telegram_chatbot_starter.model.BasicInfo;
import com.weather.telegram_chatbot_starter.repo.BasicRepo;
import com.weather.telegram_chatbot_starter.service.BasicService;

@Service
public class BasicServiceImpl implements BasicService {

	 /** The Constant LOGGER. */
	  private static final Logger LOGGER = LogManager.getLogger();
	  
	private BasicRepo basicRepo;

	@Autowired
	public BasicServiceImpl(BasicRepo studentRepo) {
		this.basicRepo = studentRepo;
	}

	@Override
	@Transactional
	public BasicInfoDto addNewOrGetExisting(String name) {
		Optional<BasicInfo> student = basicRepo.findByName(name);
		if (student.isPresent()) {
			LOGGER.info(() -> String.format("The student with name %s is allready present",name));
			return toBasicInfoDto(student.get());
		} else {
			LOGGER.info(() -> String.format("The student with name %s is not present",name));
			BasicInfo basicInfo = new BasicInfo();
			basicInfo.setName(name);
			try {
				basicRepo.save(basicInfo);
				return toBasicInfoDto(basicInfo);
			} catch (DataIntegrityViolationException ex) {
				throw new IllegalArgumentException(
						String.format("The name %s already exists in this seesion, please try with other name.", name));
			}

		}
	}

	private BasicInfoDto toBasicInfoDto(BasicInfo basic) {
		return new BasicInfoDto(basic.getName());
	}

}
