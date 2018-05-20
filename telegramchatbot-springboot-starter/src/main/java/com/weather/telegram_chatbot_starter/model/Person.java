package com.weather.telegram_chatbot_starter.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This is the entity class for the Person table
 * 
 * @author stan4
 *
 */
@Entity
@Table(name = "PERSON", schema = "public")
public class Person {
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "PHONE_NUM", unique = true)
	private String phoneNumber;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Location> city = new HashSet<Location>();
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "favorite_city_id")
	private Location favoriteCity;
	
	@Column(name = "LAST_SEARCHED_CITY")
	private String lastSearchedCity;
	
	@Column(name = "NOTIFICATION_HOUR")
	private String notificationHour;
	
	public Person(String phone) {
		phoneNumber = phone;
	}
	
	public Person() {
		
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Set<Location> getCity() {
		return city;
	}
	
	public void setCity(Set<Location> city) {
		this.city = city;
	}
	
	public Location getFavoriteCity() {
		return favoriteCity;
	}
	
	public void setFavoriteCity(Location favoriteCity) {
		this.favoriteCity = favoriteCity;
	}
	
	public String getLastSearchedCity() {
		return lastSearchedCity;
	}
	
	public void setLastSearchedCity(String lastSearchedCity) {
		this.lastSearchedCity = lastSearchedCity;
	}
	
	public String getNotificationHour() {
		return notificationHour;
	}
	
	public void setNotificationHour(String notificationHour) {
		this.notificationHour = notificationHour;
	}
	
}
