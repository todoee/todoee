package io.todoee.wsg.config;

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
public class WsgConfig {

	private Integer port;
	private Boolean remoteMapping;
	private String pathPrefix;
	private Integer tokenExpire;
	
	private Integer instances;
	
	private static final Integer DEFAULT_PORT = 8080;
	private static final String DEFAULT_PATH_PREFIX = "";
	private static final Boolean DEFAULT_REMOTE = false;
	private static final Integer DEFAULT_TOKEN_EXPIRE = 3600;
	
	private static final int DEFAULT_INSTANCES = 1;
	
	private static final String PATH_PREFIX_KEY = "wsg.path.prefix";
	private static final String PORT_KEY = "wsg.port";
	private static final String REMOTE_KEY = "wsg.remote";
	private static final String TOKEN_EXPIRE_KEY = "wsg.token.expire";
	
	public static final String INSTANCES_KEY = "wsg.instances";
	
	@Inject
	public WsgConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
		if (config.hasPath(PATH_PREFIX_KEY)) {
			this.setPathPrefix(config.getString(PATH_PREFIX_KEY));
		} else {
			this.setPathPrefix(DEFAULT_PATH_PREFIX);
		}
		if (config.hasPath(PORT_KEY)) {
			this.setPort(config.getInt(PORT_KEY));
		} else {
			this.setPort(DEFAULT_PORT);
		}
		if (config.hasPath(REMOTE_KEY)) {
			this.setRemoteMapping(config.getBoolean(REMOTE_KEY));
		} else {
			this.setRemoteMapping(DEFAULT_REMOTE);
		}
		if (config.hasPath(TOKEN_EXPIRE_KEY)) {
			this.setTokenExpire(config.getInt(TOKEN_EXPIRE_KEY));
		} else {
			this.setTokenExpire(DEFAULT_TOKEN_EXPIRE);
		}
		
		if (config.hasPath(INSTANCES_KEY)) {
			this.setInstances(config.getInt(INSTANCES_KEY));
		} else {
			this.setInstances(DEFAULT_INSTANCES);
		}
	}
}
