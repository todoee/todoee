package io.todoee.httpclient.config;

import javax.inject.Inject;

import com.typesafe.config.Config;

import io.todoee.core.config.DoConfig;
import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Data
public class HttpClientConfig {
	
	private String address;
	private int connections;
	private int timeout;
	
	private static final String DEFAULT_ADDRESS = "";
	private static final int DEFAULT_CONNECTIONS = 5;
	private static final int DEFAULT_TIMEOUT = 20;
	
	private static final String HTTPCLIENT_ADDRESS_KEY = "httpclient.address";
	private static final String HTTPCLIENT_CONNECTIONS_KEY = "httpclient.connections";
	private static final String HTTPCLIENT_TIMEOUT_KEY = "httpclient.timeout";
	
	@Inject
	public HttpClientConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
		if (config.hasPath(HTTPCLIENT_ADDRESS_KEY)) {
			this.setAddress(config.getString(HTTPCLIENT_ADDRESS_KEY));
		} else {
			this.setAddress(DEFAULT_ADDRESS);
		}
		if (config.hasPath(HTTPCLIENT_CONNECTIONS_KEY)) {
			this.setConnections(config.getInt(HTTPCLIENT_CONNECTIONS_KEY));
		} else {
			this.setConnections(DEFAULT_CONNECTIONS);
		}
		if (config.hasPath(HTTPCLIENT_TIMEOUT_KEY)) {
			this.setTimeout(config.getInt(HTTPCLIENT_TIMEOUT_KEY));
		} else {
			this.setTimeout(DEFAULT_TIMEOUT);
		}
	}
}
