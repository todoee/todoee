package io.todoee.service.module;

import com.google.inject.AbstractModule;

import io.todoee.service.provider.CircuitBreakerProvider;
import io.vertx.circuitbreaker.CircuitBreaker;

/**
 * 
 * @author James.zhang
 *
 */
public class ServiceModule extends AbstractModule {
	
	@Override
    protected void configure() {
    	bind(CircuitBreaker.class).toProvider(CircuitBreakerProvider.class).asEagerSingleton();
    }
}
