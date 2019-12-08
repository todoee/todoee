package io.todoee.wsg.verticle;

import io.todoee.core.context.IocContext;
import io.todoee.core.loader.VerticleLoader;
import io.todoee.core.verticle.WsgAbstractVerticle;
import io.todoee.wsg.config.WsgConfig;

public class WsgVerticleLoader extends VerticleLoader<WsgAbstractVerticle>{

	private WsgConfig config;
	
	public WsgVerticleLoader() {
		this.config = IocContext.getBean(WsgConfig.class);
	}
	
	@Override
	protected boolean worker() {
		return false;
	}

	@Override
	protected int instances() {
		return config.getInstances();
	}

}
