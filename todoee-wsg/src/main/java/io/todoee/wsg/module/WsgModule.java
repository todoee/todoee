package io.todoee.wsg.module;

import com.google.inject.AbstractModule;

import io.todoee.wsg.config.WsgConfig;
import io.todoee.wsg.handle.ServiceMapping;
import io.todoee.wsg.handle.WsgHandler;

/**
 * 
 * @author James.zhang
 *
 */
public class WsgModule extends AbstractModule {
	
    @Override
    protected void configure() {
    	bind(WsgConfig.class).asEagerSingleton();
    	bind(ServiceMapping.class).asEagerSingleton();
    	bind(WsgHandler.class).asEagerSingleton();
    }

}
