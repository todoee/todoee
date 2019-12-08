package io.todoee.web.reflections.injectors.annot.impl;

import io.todoee.web.annotations.params.CookieParam;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

public class CookieParamInjector implements AnnotatedParamInjector<CookieParam> {

	@Override
	public Object resolve(RoutingContext context, CookieParam annotation, String paramName, Class<?> resultClass) {
		String cookieName = annotation.value();
		if ("".equals(cookieName)) {
			cookieName = paramName;
		}
		Cookie cookie = context.getCookie(cookieName);
		if (cookie == null) {
			return null;
		} else if (resultClass.equals(Cookie.class)) {
			return cookie;
		} else if (resultClass.equals(String.class)) {
			return cookie.getValue();
		} else {
			throw new RuntimeException("Cookie '" + cookieName + "' no support class: " + resultClass);
		}
	}

}
