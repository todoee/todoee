package io.todoee.web.verticle;

import io.todoee.core.context.IocContext;
import io.todoee.core.loader.VerticleLoader;
import io.todoee.core.verticle.WebAbstractVerticle;
import io.todoee.web.config.WebConfig;

public class WebVerticleLoader extends VerticleLoader<WebAbstractVerticle>{

	private WebConfig config;
	
	public WebVerticleLoader() {
		this.config = IocContext.getBean(WebConfig.class);
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
