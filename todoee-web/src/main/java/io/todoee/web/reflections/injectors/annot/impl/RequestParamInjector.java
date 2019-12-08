package io.todoee.web.reflections.injectors.annot.impl;

import io.todoee.web.annotations.params.QueryParam;
import io.todoee.web.exception.WebErrCode;
import io.todoee.web.exception.WebException;
import io.todoee.web.reflections.adapters.ParameterAdapterRegistry;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.RoutingContext;

import org.apache.commons.lang3.StringUtils;

public class RequestParamInjector implements AnnotatedParamInjector<QueryParam> {

	private final ParameterAdapterRegistry adapters;

	public RequestParamInjector(ParameterAdapterRegistry adapters) {
		this.adapters = adapters;
	}

	@Override
	public Object resolve(RoutingContext context, QueryParam annotation, String paramName, Class<?> resultClass) {
		final String requestParamName = StringUtils.isEmpty(annotation.value()) ? paramName : annotation.value();
		final String paramValue = context.request().getParam(requestParamName);

		if (paramValue == null && annotation.required()) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.REQUEST_PARAM_REQUIRED, RestException.class,
//					requestParamName);
			WebException e = new WebException(600, WebErrCode.REQUEST_PARAM_REQUIRED);
			e.throwException();
		}

		try {
			return adapters.adaptParam(paramValue, resultClass);
		} catch (IllegalArgumentException iae) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.REQUEST_PARAM_ADAPT_ERROR, RestException.class,
//					requestParamName, paramValue);
			WebException e = new WebException(600, WebErrCode.REQUEST_PARAM_ADAPT_ERROR);
			throw e;
		}
	}

}
