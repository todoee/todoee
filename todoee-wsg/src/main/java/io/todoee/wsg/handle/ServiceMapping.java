package io.todoee.wsg.handle;

import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;

import io.todoee.wsg.config.WsgConfig;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.AsyncMap;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class ServiceMapping {

	private static final String SERVICE_MAPPING_CONFIG = "mapping.properties";
	private Properties properties = new Properties();

	@Inject
	private Vertx vertx;

	private WsgConfig config;

	private AsyncMap<Object, Object> serviceMap;

	private void setServiceMap(AsyncMap<Object, Object> serviceMap) {
		this.serviceMap = serviceMap;
	}

	@Inject
	public ServiceMapping(WsgConfig config) {
		this.config = config;
		init();
	}
	
	private void init() {
		if (config.getRemoteMapping()) {
			vertx.sharedData().getAsyncMap("servicemap", resultHandler -> {
				if (resultHandler.succeeded()) {
					AsyncMap<Object, Object> amap = resultHandler.result();
					setServiceMap(amap);
				} else {
					throw new RuntimeException(resultHandler.cause());
				}
			});
		} else {
			loadLocal();
		}
	}

	public Future<String> getService(String url) {
		Future<String> future = Future.future();
		if (config.getRemoteMapping()) {
			log.debug("load service mapping in vertx ShareData");
			serviceMap.get(url, v -> {
				if (v.succeeded()) {
					String service = (String) v.result();
					future.complete(service);
				} else {
					future.fail(v.cause());
				}
			});
		} else {
			log.debug("load service mapping in local properties");
			try {
				String service = properties.getProperty(url);
				future.complete(service);
			} catch (Exception e) {
				future.fail(e);
			}
		}

		return future;
	}
	
	private void loadLocal() {
		log.debug("load local properties file " + SERVICE_MAPPING_CONFIG
				+ " in classpath");
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(SERVICE_MAPPING_CONFIG);
			properties.load(in);
		} catch (Exception e) {
			log.error("load properties file [" + SERVICE_MAPPING_CONFIG
					+ "] error: " + e.getMessage());
		}
		properties
				.entrySet()
				.stream()
				.forEach(
						entry -> log.debug("service mapping: " + entry.getKey()
								+ " --> " + entry.getValue()));
	}
}
