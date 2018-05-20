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
	
	////////////////
	
	/** The current location weather information button */
	@Value("${menu.current_location_weather_information}")
	private String currentLocationWeatherInformation;
	
	/** The another location weather information button */
	@Value("${menu.another_location_weather_information}")
	private String anotherLocationWeatherInformation;
	
	/** The search list history button */
	@Value("${menu.search_list_history}")
	private String searchListHistory;
	
	/** The application credits button */
	@Value("${menu.application_credits}")
	private String applicationCredits;
	
	/** The show forecast button */
	@Value("${menu.show_forecast}")
	private String showForecast;
	
	/** The back to menu button */
	@Value("${menu.go_back_to_menu}")
	private String goBackToMenu;
	
	/** The share contact details button */
	@Value("${menu.share_contact_details}")
	private String shareContactDetails;
	
	/** The deny button */
	@Value("${menu.deny}")
	private String deny;
	
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
	
	public String getCurrentLocationWeatherInformation() {
		return currentLocationWeatherInformation;
	}
	
	public void setCurrentLocationWeatherInformation(String currentLocationWeatherInformation) {
		this.currentLocationWeatherInformation = currentLocationWeatherInformation;
	}
	
	public String getAnotherLocationWeatherInformation() {
		return anotherLocationWeatherInformation;
	}
	
	public void setAnotherLocationWeatherInformation(String anotherLocationWeatherInformation) {
		this.anotherLocationWeatherInformation = anotherLocationWeatherInformation;
	}
	
	public String getSearchListHistory() {
		return searchListHistory;
	}
	
	public void setSearchListHistory(String searchListHistory) {
		this.searchListHistory = searchListHistory;
	}
	
	public String getApplicationCredits() {
		return applicationCredits;
	}
	
	public void setApplicationCredits(String applicationCredits) {
		this.applicationCredits = applicationCredits;
	}
	
	public String getShowForecast() {
		return showForecast;
	}
	
	public void setShowForecast(String showForecast) {
		this.showForecast = showForecast;
	}
	
	public String getGoBackToMenu() {
		return goBackToMenu;
	}
	
	public void setGoBackToMenu(String goBackToMenu) {
		this.goBackToMenu = goBackToMenu;
	}
	
	public String getShareContactDetails() {
		return shareContactDetails;
	}
	
	public void setShareContactDetails(String shareContactDetails) {
		this.shareContactDetails = shareContactDetails;
	}
	
	public String getDeny() {
		return deny;
	}
	
	public void setDeny(String deny) {
		this.deny = deny;
	}
	
}
