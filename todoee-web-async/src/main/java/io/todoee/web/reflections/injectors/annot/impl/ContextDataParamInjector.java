package io.todoee.web.reflections.injectors.annot.impl;

import io.todoee.web.annotations.params.ContextData;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.RoutingContext;

public class ContextDataParamInjector implements AnnotatedParamInjector<ContextData> {

	@Override
	public Object resolve(RoutingContext context, ContextData annotation, String paramName, Class<?> resultClass) {
		return context.data();
	}

}
