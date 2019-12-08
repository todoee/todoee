package io.todoee.config.module;

import com.google.inject.Module;

import io.todoee.core.spi.ConfigModuleFactory;

public class RemoteConfigModuleFactory implements ConfigModuleFactory {
	
	@Override
	public Module get() {
		return new RemoteConfigModule();
	}
}
