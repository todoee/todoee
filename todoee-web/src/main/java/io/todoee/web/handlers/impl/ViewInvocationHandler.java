package io.todoee.web.handlers.impl;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.todoee.core.context.IocContext;
import io.todoee.web.constants.WebConstant;
import io.todoee.web.exception.WebException;
import io.todoee.web.handlers.InvocationHandler;
import io.todoee.web.utils.GlobalStore;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.TemplateHandler;

/**
 * 
 * @author James.zhang
 *
 */
public class ViewInvocationHandler<T> extends InvocationHandler<T> {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(ViewInvocationHandler.class);

	public ViewInvocationHandler(Object instance, Method method) {
		super(instance, method);
	}

	@Override
	public void handle(RoutingContext routingContext) {
		if (routingContext.failed()) {
			LOGGER.warn("Request failed", routingContext.failure());
			return;
		}
		try {
			Object[] parameters = getParameters(routingContext);
			invoke(routingContext, parameters);
		} catch (Exception e) {
			routingContext.fail(e);
		}
	}

	private void invoke(RoutingContext context, Object[] parameters)
			throws Exception {
		Object returned = method.invoke(instance, parameters);
		
		if (context.response().headWritten()
				|| context.response().ended()) {
			LOGGER.warn("Response has already been written or end");
			return;
		}
		if (returned != null) {
			if (returned instanceof String) {
				String path = (String)returned;
				if (path.startsWith("redirect:")) {
					path = path.substring(path.indexOf("redirect:")+9);
					context.response().putHeader("location", path).setStatusCode(302).end();
					return;
				}
			} else {
				throw new WebException(600, "@View return type must is String or void");
			}
		}
		
		context.put(WebConstant.METHOD_RETURN_OBJ, returned);
		
		context.put("param", context.request().params());
		context.put("session", context.session());
		context.put("user", context.user());
		context.put("global", GlobalStore.data(context.vertx()));
		TemplateHandler templateHandler = IocContext.getBean(TemplateHandler.class);
		templateHandler.handle(context);
	}
}