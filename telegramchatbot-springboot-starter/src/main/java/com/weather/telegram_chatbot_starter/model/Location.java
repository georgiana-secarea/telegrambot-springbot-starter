package com.weather.telegram_chatbot_starter.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * This is the entity class for the Location table
 * 
 * @author stan4
 *
 */
@Entity
@Table(name = "CITY", schema = "public")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;

	@Column(name = "NAME")
	private String name;

	@ManyToMany(mappedBy = "city", cascade = CascadeType.ALL)
	private Set<Person> persons = new HashSet<>();

	public Location(String city, Set<Person> persons) {
		this.persons = persons;
		this.name = city;
	}

	public Location() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String city) {
		this.name = city;
	}

	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

}
