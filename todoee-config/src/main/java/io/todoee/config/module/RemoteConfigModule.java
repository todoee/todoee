package io.todoee.config.module;

import com.google.inject.AbstractModule;

import io.todoee.config.impl.RemoteConfig;
import io.todoee.core.config.DoConfig;

public class RemoteConfigModule extends AbstractModule {
	
	@Override
    protected void configure() {
    	bind(DoConfig.class).to(RemoteConfig.class).asEagerSingleton();
    }
}
