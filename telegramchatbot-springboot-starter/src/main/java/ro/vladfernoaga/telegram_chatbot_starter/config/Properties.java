package ro.vladfernoaga.telegram_chatbot_starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import lombok.Getter;
import lombok.Setter;


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
