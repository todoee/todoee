package io.todoee.web.routing;

import io.todoee.core.context.IocContext;
import io.todoee.web.annotations.RequestMapping;
import io.todoee.web.annotations.method.View;
import io.todoee.web.handlers.impl.ServiceInvocationHandler;
import io.todoee.web.handlers.impl.ViewInvocationHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Method;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class RouteBuilder {

	public static void build(Class<?> clazz, Router router) {
		Method[] methods = clazz.getDeclaredMethods();
		String basePath = clazz.getAnnotation(RequestMapping.class).value();

		for (Method method : methods) {
			if (!HttpMethodFactory.isRouteMethod(method)) {
				continue;
			}
			Map<HttpMethod, String[]> httpMethods = HttpMethodFactory
					.fromAnnotatedMethod(method);
			httpMethods.entrySet().stream().forEach(entry -> {
				Object instance;
				try {
					instance = clazz.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				IocContext.inject(instance);
				createRoute(instance, method, basePath, entry, router);
			});
		}
	}

	private static void createRoute(Object instance, Method method,
			String basePath, Map.Entry<HttpMethod, String[]> entry, Router router) {
		Handler<RoutingContext> handler;
		if (method.isAnnotationPresent(View.class)) {
			log.debug("Web view method '" + method.getName() + "()' return type: " + method.getReturnType());
			if (!method.getReturnType().getSimpleName().equals("void")
					&& !method.getReturnType().equals(String.class)) {
				throw new RuntimeException("method '" + method.getName() + "()' return type must be void or String");
			}
			handler = new ViewInvocationHandler(instance, method);
		} else {
			log.debug("Web api method '" + method.getName() + "()' return type: " + method.getReturnType());
			handler = new ServiceInvocationHandler(instance, method, Boolean.parseBoolean(entry.getValue()[1]));
		}
		
		HttpMethod httpMethod = entry.getKey();
		String fullPath = basePath + entry.getValue()[0];
		router.route(httpMethod, fullPath).handler(handler);
	}
}
