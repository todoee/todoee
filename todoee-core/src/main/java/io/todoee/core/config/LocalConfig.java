package io.todoee.core.config;

import javax.inject.Singleton;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Singleton
public class LocalConfig extends DoConfig {

	@Override
	protected Config loadTodoee() {
		return ConfigFactory.load(ConfigType.TODOEE_CONFIG);
	}

	@Override
	protected Config loadApplication() {
		return ConfigFactory.defaultApplication();
	}
}
