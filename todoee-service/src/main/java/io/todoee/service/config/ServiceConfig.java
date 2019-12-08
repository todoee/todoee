package io.todoee.service.config;

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
public class ServiceConfig {

	private int instances;
	private boolean worker;
	
	private static final boolean DEFAULT_WORKER = true;
	private static final int DEFAULT_INSTANCES = 1;
	
	public static final String WORKER_KEY = "service.worker";
	public static final String INSTANCES_KEY = "service.instances";
	
	@Inject
	public ServiceConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
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
