package io.todoee.service.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHandler;

/**
 * 
 * @author James.zhang
 *
 */
public class GenericServiceBinder {

	private String address;
	private List<Function<Message<JsonObject>, Future<Message<JsonObject>>>> interceptors;

	private Vertx vertx;

	public GenericServiceBinder(Vertx vertx) {
		this.vertx = vertx;
	}

	public GenericServiceBinder setAddress(String address) {
		this.address = address;
		return this;
	}

	public GenericServiceBinder addInterceptor(
			Function<Message<JsonObject>, Future<Message<JsonObject>>> interceptor) {
		if (interceptors == null) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(interceptor);
		return this;
	}

	public void unregister(MessageConsumer<JsonObject> consumer) {
		Objects.requireNonNull(consumer);

		if (consumer instanceof ProxyHandler) {
			((ProxyHandler) consumer).close();
		} else {
			consumer.unregister();
		}
	}

	public <T> MessageConsumer<JsonObject> register(Class<T> clazz, T service) {
		ProxyHandler handler = new GenericServiceProxyHandler<T>(service);
		return handler.register(vertx.eventBus(), address, interceptors);
	}

}
