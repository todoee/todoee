package io.todoee.web.config;

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
public class WebConfig {

	private Integer port;

	private Boolean templateCache;
	
	private Boolean staticCache;
	
	private long sessionTimeout;
	
	private String home;
	
	private String login;
	
	private String privatePrefix;
	
	private Boolean auth;
	
	private String usernameKey;
	private String passwordKey;
	
	private int instances;
	private boolean worker;
	
	private static final boolean DEFAULT_WORKER = true;
	private static final int DEFAULT_INSTANCES = 1;
	
	private static final int DEFAULT_PORT = 8080;
	private static final boolean DEFAULT_TEMPLATE_CACHE = false;
	private static final boolean DEFAULT_STATIC_CACHE = false;
	private static final long DEFAULT_SESSION_TIMEOUT = 3600000;
	private static final String DEFAULT_PAGE_HOME = "/apidoc/doc";
	private static final String DEFAULT_PAGE_LOGIN = "/";
	private static final String DEFAULT_PRIVATE_PREFIX = "demo";
	private static final boolean DEFAULT_AUTH_ENABLED = false;
	private static final String DEFAULT_AUTH_USERNAMEKEY = "username";
	private static final String DEFAULT_AUTH_PASSWORDKEY = "password";
	
	private static final String PORT_KEY = "web.port";
	private static final String TEMPLATE_CACHE_KEY = "web.template.cache";
	private static final String STATIC_CACHE_KEY = "web.static.cache";
	private static final String SESSION_TIMEOUT_KEY = "web.session.timeout";
	private static final String PAGE_HOME_KEY = "web.page.home";
	private static final String PAGE_LOGIN_KEY = "web.page.login";
	private static final String PRIVATE_PREFIX_KEY = "web.private.prefix";
	private static final String AUTH_ENABLED_KEY = "web.auth.enabled";
	private static final String AUTH_USERNAMEKEY_KEY = "web.auth.usernamekey";
	private static final String AUTH_PASSWORDKEY_KEY = "web.auth.passwordkey";
	
	private static final String WORKER_KEY = "web.worker";
	private static final String INSTANCES_KEY = "web.instances";
	
	@Inject
	public WebConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
		if (config.hasPath(PORT_KEY)) {
			this.setPort(config.getInt(PORT_KEY));
		} else {
			this.setPort(DEFAULT_PORT);
		}
		if (config.hasPath(TEMPLATE_CACHE_KEY)) {
			this.setTemplateCache(config.getBoolean(TEMPLATE_CACHE_KEY));
		} else {
			this.setTemplateCache(DEFAULT_TEMPLATE_CACHE);
		}
		if (config.hasPath(STATIC_CACHE_KEY)) {
			this.setStaticCache(config.getBoolean(STATIC_CACHE_KEY));
		} else {
			this.setStaticCache(DEFAULT_STATIC_CACHE);
		}
		if (config.hasPath(SESSION_TIMEOUT_KEY)) {
			this.setSessionTimeout(config.getLong(SESSION_TIMEOUT_KEY));
		} else {
			this.setSessionTimeout(DEFAULT_SESSION_TIMEOUT);
		}
		if (config.hasPath(PAGE_HOME_KEY)) {
			this.setHome(config.getString(PAGE_HOME_KEY));
		} else {
			this.setHome(DEFAULT_PAGE_HOME);
		}
		if (config.hasPath(PAGE_LOGIN_KEY)) {
			this.setLogin(config.getString(PAGE_LOGIN_KEY));
		} else {
			this.setLogin(DEFAULT_PAGE_LOGIN);
		}
		if (config.hasPath(PRIVATE_PREFIX_KEY)) {
			this.setPrivatePrefix(config.getString(PRIVATE_PREFIX_KEY));
		} else {
			this.setPrivatePrefix(DEFAULT_PRIVATE_PREFIX);
		}
		if (config.hasPath(AUTH_ENABLED_KEY)) {
			this.setAuth(config.getBoolean(AUTH_ENABLED_KEY));
		} else {
			this.setAuth(DEFAULT_AUTH_ENABLED);
		}
		if (config.hasPath(AUTH_USERNAMEKEY_KEY)) {
			this.setUsernameKey(config.getString(AUTH_USERNAMEKEY_KEY));
		} else {
			this.setUsernameKey(DEFAULT_AUTH_USERNAMEKEY);
		}
		if (config.hasPath(AUTH_PASSWORDKEY_KEY)) {
			this.setPasswordKey(config.getString(AUTH_PASSWORDKEY_KEY));
		} else {
			this.setPasswordKey(DEFAULT_AUTH_PASSWORDKEY);
		}
		
		if (config.hasPath(WORKER_KEY)) {
			this.setWorker(config.getBoolean(WORKER_KEY));
		} else {
			this.setWorker(DEFAULT_WORKER);
		}
		if (config.hasPath(INSTANCES_KEY)) {
			this.setInstances(config.getInt(INSTANCES_KEY));
		} else {
			this.setInstances(DEFAULT_INSTANCES);
		}
	}
}
