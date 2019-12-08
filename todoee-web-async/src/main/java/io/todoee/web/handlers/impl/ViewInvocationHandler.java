package io.todoee.web.handlers.impl;

import java.lang.reflect.Method;

import io.todoee.core.context.IocContext;
import io.todoee.web.constants.WebConstant;
import io.todoee.web.exception.WebException;
import io.todoee.web.handlers.InvocationHandler;
import io.todoee.web.utils.GlobalStore;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.TemplateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class ViewInvocationHandler extends InvocationHandler {

	public ViewInvocationHandler(Object instance, Method method) {
		super(instance, method);
	}

	@Override
	public void handle(RoutingContext context) {
		if (context.failed()) {
			log.warn("Request failed", context.failure());
			return;
		}
		Object[] parameters = getParameters(context);
		invoke(context, parameters);
	}

	private void invoke(RoutingContext context, Object[] parameters) {
		Object value;
		try {
			value = method.invoke(instance, parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (value instanceof Future) {
			((Future<?>) value).setHandler(h -> {
				if (h.succeeded()) {
					if (context.response().headWritten() || context.response().ended()) {
						log.warn("Response has already been written or end");
					} else {
						Object returned = h.result();
						if (returned != null) {
							context.put(WebConstant.METHOD_RETURN_OBJ, returned);
							String path = (String) returned;
							if (path.startsWith("redirect:")) {
								path = path.substring(path.indexOf("redirect:") + 9);
								context.response().putHeader("location", path).setStatusCode(302).end();
								return;
							}
						}

						context.put("param", context.request().params());
						context.put("session", context.session());
						context.put("user", context.user());
						context.put("global", GlobalStore.data(context.vertx()));
						IocContext.getBean(TemplateHandler.class).handle(context);
					}
				} else {
					context.fail(h.cause());
				}
			});
		} else {
			context.fail(new WebException("method: " + method.getName() + " Return value must be Future<?> type"));
		}
	}
}