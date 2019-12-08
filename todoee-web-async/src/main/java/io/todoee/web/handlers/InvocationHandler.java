package io.todoee.web.handlers;

import io.todoee.web.reflections.injectors.InjectorRegistryFactory;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjectorRegistry;
import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.todoee.web.reflections.injectors.typed.TypedParamInjectorRegistry;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author James.zhang
 *
 */
public abstract class InvocationHandler implements Handler<RoutingContext> {

	protected final Method method;
	protected final Object instance;
	private final Parameter[] parameters;

	protected InvocationHandler(Object instance, Method method) {
		this.method = method;
		parameters = method.getParameters();
		this.instance = instance;
	}

	@Override
	abstract public void handle(RoutingContext routingContext);

	protected Object[] getParameters(RoutingContext routingContext) {
		List<Object> params = new ArrayList<>();
		for (Parameter param : parameters) {
			Object paramInstance;
			try {
				paramInstance = getParameterInstance(routingContext, param.getAnnotations(), param.getType(),
						param.getName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			params.add(paramInstance);
		}
		return params.toArray();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getParameterInstance(RoutingContext context, Annotation[] annotations, Class<?> parameterClass,
			String paramName) throws Exception {
		final TypedParamInjectorRegistry typeInjectors = InjectorRegistryFactory.typeInjectors;
		if (annotations.length == 0) {
			final ParamInjector<?> injector = typeInjectors.getInjector(parameterClass);
			if (injector == null) {
				return null;
			}
			return injector.resolve(context);
		}
		if (annotations.length > 1) {
			throw new IllegalArgumentException("Every parameter should only have ONE annotation");
		}
		final Annotation annotation = annotations[0];
		final AnnotatedParamInjectorRegistry annotatedInjectors = InjectorRegistryFactory.annotInjectors;
		final AnnotatedParamInjector injector = annotatedInjectors.getInjector(annotation.annotationType());
		if (injector == null) {
			return null;
		}
		return injector.resolve(context, annotation, paramName, parameterClass);
	}
}