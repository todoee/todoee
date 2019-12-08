package io.todoee.dao.config;

import javax.inject.Inject;

import com.google.inject.Singleton;
import com.typesafe.config.Config;

import io.todoee.core.config.DoConfig;
import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Singleton
@Data
public class DaoConfig {
	
	private String driver;
	private String url;
	private String username;
	private String password;
	private int maximumPoolSize;
	
	public static final String DRIVER_KEY = "dao.driver";
	public static final String URL_KEY = "dao.url";
	public static final String MAXIMUMPOOLSIZE_KEY = "dao.maximumPoolSize";
	public static final String USERNAME_KEY = "dao.username";
	public static final String PASSWORD_KEY = "dao.password";
	
	private static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DEFAULT_URL = "jdbc:mysql://0.0.0.0:3306/todoee?characterEncoding=UTF-8&useSSL=false";
	private static final int DEFAULT_MAXIMUMPOOLSIZE = 5;
	private static final String DEFAULT_USERNAME = "root";
	private static final String DEFAULT_PASSWORD = "123456";
	
	@Inject
	public DaoConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
		if (config.hasPath(DRIVER_KEY)) {
			this.setDriver(config.getString(DRIVER_KEY));
		} else {
			this.setDriver(DEFAULT_DRIVER);
		}
		if (config.hasPath(URL_KEY)) {
			this.setUrl(config.getString(URL_KEY));
		} else {
			this.setUrl(DEFAULT_URL);
		}
		if (config.hasPath(USERNAME_KEY)) {
			this.setUsername(config.getString(USERNAME_KEY));
		} else {
			this.setUsername(DEFAULT_USERNAME);
		}
		if (config.hasPath(PASSWORD_KEY)) {
			this.setPassword(config.getString(PASSWORD_KEY));
		} else {
			this.setPassword(DEFAULT_PASSWORD);
		}
		if (config.hasPath(MAXIMUMPOOLSIZE_KEY)) {
			this.setMaximumPoolSize(config.getInt(MAXIMUMPOOLSIZE_KEY));
		} else {
			this.setMaximumPoolSize(DEFAULT_MAXIMUMPOOLSIZE);
		}
	}
}
