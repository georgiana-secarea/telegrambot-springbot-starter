package com.weather.telegram_chatbot_starter.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	private Set<City> city=new HashSet<City>();

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "favorite_city_id")
	private City favoriteCity;

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

	public int getUserId() {
		return id;
	}

	public void setUserId(int i) {
		this.id = i;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<City> getCity() {
		return city;
	}

	public void setCity(Set<City> city) {
		this.city = city;
	}

	public City getFavoriteCity() {
		return favoriteCity;
	}

	public void setFavoriteCity(City favoriteCity) {
		this.favoriteCity = favoriteCity;
	}

}
