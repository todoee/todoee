package io.todoee.core.config;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.google.inject.ImplementedBy;
import com.typesafe.config.Config;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@ImplementedBy(LocalConfig.class)
@Slf4j
public abstract class DoConfig {
	private String lock = "lock";

	private final ScheduledExecutorService listenService = Executors.newSingleThreadScheduledExecutor();

	private static final long SCAN_PERIOD = 10000;

	private Config todoeeConfig;
	private Config applicationConfig;

	protected abstract Config loadTodoee();

	protected abstract Config loadApplication();

	public DoConfig() {
		this.todoeeConfig = loadTodoee();
		this.applicationConfig = loadApplication();
		startListen();
	}

	public Config todoee() {
		return todoeeConfig;
	}

	public Config application() {
		synchronized (lock) {
			return applicationConfig;
		}
	}

	private void startListen() {
		listenService.scheduleAtFixedRate(() -> {
			Config newConfig = loadApplication();
			String newConfigJson = getConfigJson(newConfig);
			String oldConfigJson = getConfigJson(applicationConfig);
			if (!newConfigJson.equals(oldConfigJson)) {
				synchronized (lock) {
					applicationConfig = newConfig;
				}
				log.debug("config changed, refresh application config");
			}
		}, SCAN_PERIOD, SCAN_PERIOD, TimeUnit.MILLISECONDS);
	}

	private static String getConfigJson(Config newConfig) {
		Map<String, Object> map = new TreeMap<>();
		newConfig.entrySet().forEach(entry -> {
			map.put(entry.getKey(), entry.getValue().render());
		});
		return JSON.toJSONString(map);
	}
}
