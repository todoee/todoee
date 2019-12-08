package io.todoee.web.reflections.injectors.annot.impl;

import io.todoee.web.annotations.params.HeaderParam;
import io.todoee.web.exception.WebErrCode;
import io.todoee.web.exception.WebException;
import io.todoee.web.reflections.adapters.ParameterAdapterRegistry;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.RoutingContext;

public class HeaderParamInjector implements AnnotatedParamInjector<HeaderParam> {

	private final ParameterAdapterRegistry registry;

	public HeaderParamInjector(ParameterAdapterRegistry registry) {
		this.registry = registry;
	}

	@Override
	public Object resolve(RoutingContext context, HeaderParam annotation, String paramName, Class<?> resultClass) {
		String headerName = annotation.value();
		if ("".equals(headerName)) {
			headerName = paramName;
		}
		String headerValue = context.request().getHeader(headerName);
		if (headerValue == null && annotation.required()) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.HEADER_PARAM_REQUIRED, RestException.class,
//					headerName);
			WebException e = new WebException(600, WebErrCode.HEADER_PARAM_REQUIRED);
			e.throwException();
		}
		try {
			return registry.adaptParam(headerValue, resultClass);
		} catch (IllegalArgumentException iae) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.HEADER_PARAM_ADAPT_ERROR, RestException.class,
//					headerName, headerValue);
			WebException e = new WebException(600, WebErrCode.HEADER_PARAM_ADAPT_ERROR);
			throw e;
		}
	}

}
