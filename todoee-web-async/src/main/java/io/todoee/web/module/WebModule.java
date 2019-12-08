package io.todoee.web.module;

import com.google.inject.AbstractModule;

import io.todoee.web.config.WebConfig;
import io.todoee.web.template.BeetlTemplateEngineImpl;
import io.todoee.web.template.BeetlTemplateHandler;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.TemplateHandler;

/**
 * 
 * @author James.zhang
 *
 */
public class WebModule extends AbstractModule {
	
    @Override
    protected void configure() {
        bind(TemplateEngine.class).to(BeetlTemplateEngineImpl.class).asEagerSingleton();
        bind(WebConfig.class).asEagerSingleton();
        bind(TemplateHandler.class).to(BeetlTemplateHandler.class).asEagerSingleton();
    }

}
