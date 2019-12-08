package io.todoee.faces.config;

import javax.inject.Inject;
import javax.inject.Singleton;

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
public class FacesConfig {
	private final static String DEFAULT_SERVER_HOST = "0.0.0.0";
	private final static int DEFAULT_SERVER_PORT = 8080;
	private final static String DEFAULT_CONTEXT_PATH = "/";
	
	private String host = DEFAULT_SERVER_HOST;
	private int port = DEFAULT_SERVER_PORT;
	private String contextPath = DEFAULT_CONTEXT_PATH;
	
	@Inject
	public FacesConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
		if (config.hasPath(ConfigKey.HOST_KEY)) {
			setHost(config.getString(ConfigKey.HOST_KEY));
		}
		if (config.hasPath(ConfigKey.PORT_KEY)) {
			setPort(config.getInt(ConfigKey.PORT_KEY));
		}
		if (config.hasPath(ConfigKey.CONTEXT_PATH_KEY)) {
			setContextPath(config.getString(ConfigKey.CONTEXT_PATH_KEY));
		}
	}
}
