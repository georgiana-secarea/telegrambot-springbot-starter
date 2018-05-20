package com.weather.telegram_chatbot_starter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is the entity class for the Advice table
 * 
 * @author stan4
 *
 */
@Entity
@Table(name = "ADVICE", schema = "public")
public class Advice {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "LANGUAGE")
	private String language;
	
	@Column(name = "CONDITION", unique = true)
	private String condition;
	
	@Column(name = "EMOJI")
	private String emoji;
	
	@Column(name = "MESSAGE", unique = true)
	private String message;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public String getEmoji() {
		return emoji;
	}
	
	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
