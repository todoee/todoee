package io.todoee.web.reflections.injectors.typed;

import io.todoee.web.reflections.injectors.typed.impl.*;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.SocketAddress;
//import io.vertx.ext.auth.User;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import java.util.HashMap;
import java.util.Map;

public class TypedParamInjectorRegistry {

	private final Map<Class<?>, ParamInjector<?>> map;

	public TypedParamInjectorRegistry() {
		map = new HashMap<>();
		registerInjector(Vertx.class, new VertxParamInjector());
		registerInjector(Session.class, new SessionParamInjector());
//		registerInjector(User.class, new UserParamInjector());
		registerInjector(RoutingContext.class, new RoutingContextParamInjector());
		registerInjector(EventBus.class, new EventBusParamInjector());
		registerInjector(FileUpload[].class, new FileParamInjector());
		registerInjector(HttpServerRequest.class, new HttpRequestParamInjector());
		registerInjector(HttpServerResponse.class, new HttpResponseParamInjector());
		registerInjector(SocketAddress.class, new SocketAddressParamInjector());
	}

	private <T> void registerInjector(Class<? extends T> clazz, ParamInjector<T> injector) {
		map.put(clazz, injector);
	}

	@SuppressWarnings("unchecked")
	public <T> ParamInjector<T> getInjector(Class<? extends T> clazz) {
		return (ParamInjector<T>) map.get(clazz);
	}
}
