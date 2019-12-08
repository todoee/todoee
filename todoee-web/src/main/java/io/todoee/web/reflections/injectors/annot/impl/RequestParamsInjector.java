package io.todoee.web.reflections.injectors.annot.impl;

import io.todoee.web.annotations.params.QueryParams;
import io.todoee.web.exception.WebErrCode;
import io.todoee.web.exception.WebException;
import io.todoee.web.reflections.adapters.ParameterAdapterRegistry;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.RoutingContext;

public class RequestParamsInjector implements AnnotatedParamInjector<QueryParams> {

	private final ParameterAdapterRegistry adapters;

	public RequestParamsInjector(ParameterAdapterRegistry adapters) {
		this.adapters = adapters;
	}

	@Override
	public Object resolve(RoutingContext context, QueryParams annotation, String paramName, Class<?> resultClass) {
		try {
			return adapters.adaptParams(context.request().params(), resultClass);
		} catch (IllegalArgumentException iae) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.REQUEST_PARAMS_ADAPT_ERROR, RestException.class);
			WebException e = new WebException(600, WebErrCode.REQUEST_PARAMS_ADAPT_ERROR);
			throw e;
		}
	}

}
