package io.todoee.web.reflections.injectors.annot;

import io.vertx.ext.web.RoutingContext;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface AnnotatedParamInjector<T extends Annotation> {
	Object resolve(RoutingContext context, T annotation, String paramName, Class<?> resultClass);
}
