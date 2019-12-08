package io.todoee.core.boot;

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
public class BootConfig {

	public static final String SERVICE_HOST_KEY = "service.host";
	public static final String SERVICE_GROUP_KEY = "service.group";
	public static final String SERVICE_INSTANCES_KEY = "service.instances";
	
	public static final String WSG_INSTANCES_KEY = "wsg.instances";
	
	public static final String WEB_INSTANCES_KEY = "web.instances";
	
	private static final String DEFAULT_SERVICE_HOST = "0.0.0.0";
	private static final String DEFAULT_SERVICE_GROUP = "";
	private static final int DEFAULT_SERVICE_INSTANCES = 1;
	
	private static final int DEFAULT_WSG_INSTANCES = 1;
	
	private static final int DEFAULT_WEB_INSTANCES = 1;
	
	private String serviceHost;
	private String serviceGroup;
	
	private Integer serviceInstances;
	private Boolean serviceWorker;
	private Integer wsgInstances;
	private Integer webInstances;
	
	@Inject
	public BootConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
		if (config.hasPath(SERVICE_HOST_KEY)) {
			this.setServiceHost(config.getString(SERVICE_HOST_KEY));
		} else {
			this.setServiceHost(DEFAULT_SERVICE_HOST);
		}
		if (config.hasPath(SERVICE_GROUP_KEY)) {
			this.setServiceGroup(config.getString(SERVICE_GROUP_KEY));
		} else {
			this.setServiceGroup(DEFAULT_SERVICE_GROUP);
		}
		if (config.hasPath(SERVICE_INSTANCES_KEY)) {
			this.setServiceInstances(config.getInt(SERVICE_INSTANCES_KEY));
		} else {
			this.setServiceInstances(DEFAULT_SERVICE_INSTANCES);
		}
		if (config.hasPath(WSG_INSTANCES_KEY)) {
			this.setWsgInstances(config.getInt(WSG_INSTANCES_KEY));
		} else {
			this.setWsgInstances(DEFAULT_WSG_INSTANCES);
		}
		if (config.hasPath(WEB_INSTANCES_KEY)) {
			this.setWebInstances(config.getInt(WEB_INSTANCES_KEY));
		} else {
			this.setWebInstances(DEFAULT_WEB_INSTANCES);
		}
	}
}
