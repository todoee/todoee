package io.todoee.service.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.ThreadContext;

import com.alibaba.fastjson.JSON;

import io.todoee.base.constants.GlobalConstant;
import io.todoee.base.utils.ClassUtil;
import io.todoee.core.context.IocContext;
import io.todoee.core.exception.ServiceException;
import io.todoee.service.util.BeanValidator;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class GenericServiceProxyHandler<T> extends ProxyHandler {

	private final T service;
	private final static Map<String, Method> methodMap = new HashMap<>();

	public GenericServiceProxyHandler(T service) {
		this.service = service;

		Method[] methods = service.getClass().getDeclaredMethods();
		for (Method method : methods) {
			methodMap.put(method.getName(), method);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void handle(Message<JsonObject> msg) {
		String traceId = msg.headers().get(GlobalConstant.TRACE_ID);
		ThreadContext.put(GlobalConstant.TRACE_ID, traceId);

		String currentUser = msg.headers().get(GlobalConstant.CURRENT_USER);
		ThreadContext.put(GlobalConstant.CURRENT_USER, currentUser);

		JsonObject json = msg.body();
		String body = json.getString("body");
		String action = msg.headers().get("action");

		log.debug("recieve msg, body json string: " + body + ", action: " + action);

		if (action == null) {
			msg.reply(new ServiceException(500, "action not specified in header"));
			return;
		}

		Method method = methodMap.get(action);
		if (method == null) {
			msg.reply(new ServiceException(500, "called service not found method: " + action));
			return;
		}

		log.debug("proxy handler call method: " + method.getName());

		long timestamp = System.currentTimeMillis();
		Class<?> paramType = getParamType(method);
		log.debug("param type: " + paramType);

		IocContext.getBean(CircuitBreaker.class).execute(x -> {
			Object result;
			try {
				if (paramType.equals(Void.class)) {
					result = method.invoke(service);
				} else {
					Object paramObj = JSON.parseObject(body, paramType);
					if (!ClassUtil.isJavaClass(paramType)) {
						BeanValidator.validate(paramObj);
					}
					result = method.invoke(service, paramObj);
				}
				Future future = (Future) result;
				future.setHandler(x);
			} catch (InvocationTargetException e) {
				Throwable t = e.getTargetException();
				log.error("invocation method target exception", t);
				if (t instanceof ServiceException) {
					msg.reply((ServiceException) t);
				} else {
					msg.reply(new ServiceException(500, t.getMessage()));
				}
			} catch (Throwable t) {
				log.error("other exception", t);
				msg.reply(new ServiceException(500, t.getMessage()));
			}
		}).setHandler(createHandler(msg, timestamp, method.getName()));
	}

	private Class<?> getParamType(Method method) {
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length == 0) {
			return Void.class;
		} else {
			return paramTypes[0];
		}
	}

	private <R> Handler<AsyncResult<R>> createHandler(Message<JsonObject> msg, long timestamp, String method) {
		return res -> {
			if (res.failed()) {
				log.error("return error", res.cause());
				if (res.cause() instanceof ServiceException) {
					msg.reply(res.cause());
				} else {
					msg.reply(new ServiceException(500, "Called Service Internal Error"));
				}
			} else {
				R r = res.result();
				String result = JSON.toJSONString(r);
				log.debug("json string return value: " + result);
				msg.reply(result);
			}
			log.debug(this.service.getClass() + "::" + method + "() in [" + (System.currentTimeMillis() - timestamp)
					+ "]ms");
		};
	}
}
