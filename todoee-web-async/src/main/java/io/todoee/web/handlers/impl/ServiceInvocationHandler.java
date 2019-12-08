package io.todoee.web.handlers.impl;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.todoee.core.context.IocContext;
import io.todoee.web.constants.WebConstant;
import io.todoee.web.exception.WebException;
import io.todoee.web.handlers.InvocationHandler;
import io.todoee.web.result.Payload;
import io.todoee.web.result.ResultArray;
import io.todoee.web.routing.handler.ReturnHandler;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class ServiceInvocationHandler extends InvocationHandler {

	private final boolean payload;

	public ServiceInvocationHandler(Object instance, Method method, boolean payload) {
		super(instance, method);
		this.payload = payload;
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
						context.put(WebConstant.METHOD_RETURN_OBJ, returned);
						context.put(WebConstant.PAYLOAD_ENABLE, payload);

						Handler<RoutingContext> defaultReturnHandler = buildDefaultReturnHandler();
						Handler<RoutingContext> returnHandler = IocContext.getBean(ReturnHandler.class);
						returnHandler = returnHandler == null ? defaultReturnHandler : returnHandler;
						returnHandler.handle(context);
					}
				} else {
					context.fail(h.cause());
				}
			});
		} else {
			context.fail(new WebException("method: " + method.getName() + " Return value must be Future<?> type"));
		}
	}

	private Handler<RoutingContext> buildDefaultReturnHandler() {
		Handler<RoutingContext> defaultReturnHandler = (routeContext) -> {
			HttpServerResponse response = routeContext.response();
			Object returnObj = routeContext.get(WebConstant.METHOD_RETURN_OBJ);

			String result;
			boolean payloadEnable = routeContext.get(WebConstant.PAYLOAD_ENABLE);
			if (payloadEnable) {
				Payload payload = Payload.getInstance(returnObj);
				if (returnObj instanceof ResultArray) {
					ResultArray resultArray = (ResultArray) returnObj;
					payload.setTotal(resultArray.getTotal());
					payload.setData(resultArray.getItem());
				} else {
					payload.setData(returnObj);
				}
				String action = routeContext.get("action");
				if (action != null) {
					payload.setAction(action);
				}
				result = JSON.toJSONString(payload);
			} else {
				result = JSON.toJSONString(returnObj);
			}
			response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
			response.end(result);
		};
		return defaultReturnHandler;
	}
}
