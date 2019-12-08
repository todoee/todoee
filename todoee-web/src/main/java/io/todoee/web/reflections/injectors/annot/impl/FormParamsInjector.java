package io.todoee.web.reflections.injectors.annot.impl;

import io.todoee.web.annotations.params.FormParams;
import io.todoee.web.exception.WebErrCode;
import io.todoee.web.exception.WebException;
import io.todoee.web.reflections.adapters.ParameterAdapterRegistry;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.RoutingContext;

public class FormParamsInjector implements AnnotatedParamInjector<FormParams> {

	private final ParameterAdapterRegistry adapters;

	public FormParamsInjector(ParameterAdapterRegistry adapters) {
		this.adapters = adapters;
	}

	@Override
	public Object resolve(RoutingContext context, FormParams annotation, String paramName, Class<?> resultClass) {
		try {
			return adapters.adaptParams(context.request().formAttributes(), resultClass);
		} catch (IllegalArgumentException iae) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.FORM_ATTRIBUTES_ADAPT_ERROR, RestException.class);
			WebException e = new WebException(600, WebErrCode.FORM_ATTRIBUTES_ADAPT_ERROR);
			throw e;
		}
	}

}
