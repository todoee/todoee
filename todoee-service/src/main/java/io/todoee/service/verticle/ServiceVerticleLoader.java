package io.todoee.service.verticle;

import io.todoee.core.context.IocContext;
import io.todoee.core.loader.VerticleLoader;
import io.todoee.core.verticle.ServiceAbstractVerticle;
import io.todoee.service.config.ServiceConfig;

public class ServiceVerticleLoader extends VerticleLoader<ServiceAbstractVerticle>{

	private ServiceConfig config;
	
	public ServiceVerticleLoader() {
		this.config = IocContext.getBean(ServiceConfig.class);
	}
	
	@Override
	protected boolean worker() {
		return config.isWorker();
	}

	@Override
	protected int instances() {
		return config.getInstances();
	}

}
