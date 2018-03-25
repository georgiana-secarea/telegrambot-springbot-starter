package com.weather.telegram_chatbot_starter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LOCATION", schema="public")
public class Location {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
	private long id;
	
	@Column(name = "LAT")
	private double latitude;

	@Column(name = "LONG")
	private double longitude;
	
	@Column(name = "CITY")
	private String city;
}
