package com.weather.telegram_chatbot_starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * This class represents the application properties configuration
 * 
 * @author stan4
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
public class Properties {
	/** The hibernate auto. */
	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateAuto;

	/** The hibernate show sql. */
	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;

	/** The hibernate dialect. */
	@Value("${hibernate.dialect}")
	private String hibernateDialect;

	/** The jdbc driver class name. */
	@Value("${datasource.driverClassName}")
	private String jdbcDriverClassName;

	/** The jdbc url. */
	@Value("${datasource.url}")
	private String jdbcUrl;

	/** The jdbc username. */
	@Value("${datasource.username}")
	private String jdbcUsername;

	/** The jdbc password. */
	@Value("${datasource.password}")
	private String jdbcPassword;

	/** The bot API key. */
	@Value("${key.bot_api}")
	private String botApiKey;

	/** The weather API key. */
	@Value("${key.weather_api}")
	private String weatherApiKey;

	/** The geocoding API key. */
	@Value("${key.geocoding_api}")
	private String geocodingApiKey;

	/** The days forecast search count. */
	@Value("${forecast.daily_hours}")
	private int dailyHours;

	/** The next search hours interval. */
	@Value("${forecast.next_search_hours_interval}")
	private int nextSearchHoursInterval;

	/** The next three days forecast entries list. */
	@Value("${forecast.next_three_days_entries_count}")
	private int nextThreeDaysEntriesCount;

	/** The daily information gather count. */
	@Value("${forecast.daily_gather_count}")
	private int weatherGatherCount;

	public int getDailyHours() {
		return dailyHours;
	}

	public void setDailyHours(int dailyHours) {
		this.dailyHours = dailyHours;
	}

	public int getNextSearchHoursInterval() {
		return nextSearchHoursInterval;
	}

	public void setNextSearchHoursInterval(int nextSearchHoursInterval) {
		this.nextSearchHoursInterval = nextSearchHoursInterval;
	}

	public int getNextThreeDaysEntriesCount() {
		return nextThreeDaysEntriesCount;
	}

	public void setNextThreeDaysEntriesCount(int nextThreeDaysEntriesCount) {
		this.nextThreeDaysEntriesCount = nextThreeDaysEntriesCount;
	}

	public int getWeatherGatherCount() {
		return weatherGatherCount;
	}

	public void setWeatherGatherCount(int weatherGatherCount) {
		this.weatherGatherCount = weatherGatherCount;
	}

	public String getBotApiKey() {
		return botApiKey;
	}

	public void setBotApiKey(String botApiKey) {
		this.botApiKey = botApiKey;
	}

	public String getWeatherApiKey() {
		return weatherApiKey;
	}

	public void setWeatherApiKey(String weatherApiKey) {
		this.weatherApiKey = weatherApiKey;
	}

	public String getGeocodingApiKey() {
		return geocodingApiKey;
	}

	public void setGeocodingApiKey(String geocodingApiKey) {
		this.geocodingApiKey = geocodingApiKey;
	}

	public String getHibernateAuto() {
		return hibernateAuto;
	}

	public void setHibernateAuto(String hibernateAuto) {
		this.hibernateAuto = hibernateAuto;
	}

	public String getHibernateShowSql() {
		return hibernateShowSql;
	}

	public void setHibernateShowSql(String hibernateShowSql) {
		this.hibernateShowSql = hibernateShowSql;
	}

	public String getHibernateDialect() {
		return hibernateDialect;
	}

	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}

	public String getJdbcDriverClassName() {
		return jdbcDriverClassName;
	}

	public void setJdbcDriverClassName(String jdbcDriverClassName) {
		this.jdbcDriverClassName = jdbcDriverClassName;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

}
