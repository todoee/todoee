package io.todoee.web.handlers.impl;

import java.lang.reflect.Method;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.todoee.core.context.IocContext;
import io.todoee.web.constants.WebConstant;
import io.todoee.web.handlers.InvocationHandler;
import io.todoee.web.routing.handler.ReturnHandler;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * 
 * @author James.zhang
 *
 */
public class ServiceInvocationHandler<T> extends
		InvocationHandler<T> {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(ServiceInvocationHandler.class);

	private final boolean payload;
	
	public ServiceInvocationHandler(Object instance, Method method, boolean payload) {
		super(instance, method);
		this.payload = payload;
	}

	@Override
	public void handle(RoutingContext context) {
		if (context.failed()) {
			LOGGER.warn("Request failed", context.failure());
			return;
		}
		try {
			Object[] parameters = getParameters(context);
			invoke(context, parameters);
		} catch (Exception e) {
			context.fail(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void invoke(RoutingContext context, Object[] parameters)
			throws Exception {
		T returned = (T) method.invoke(instance, parameters);
		
		if (context.response().headWritten()
				|| context.response().ended()) {
			LOGGER.warn("Response has already been written or end");
			return;
		}
		
		context.put(WebConstant.METHOD_RETURN_OBJ, returned);
		context.put(WebConstant.PAYLOAD_ENABLE, payload);

		Handler returnHandler = IocContext.getBean(ReturnHandler.class);
		ServiceLoader<ReturnHandler> loaders = ServiceLoader
				.load(ReturnHandler.class);
		for (Handler handler : loaders) {
			LOGGER.debug("service load return handler: " + handler.getClass());
			returnHandler = handler;
		}
		returnHandler.handle(context);
	}
}