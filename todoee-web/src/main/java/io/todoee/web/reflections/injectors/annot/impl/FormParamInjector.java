package io.todoee.web.reflections.injectors.annot.impl;

import io.todoee.web.annotations.params.FormParam;
import io.todoee.web.exception.WebErrCode;
import io.todoee.web.exception.WebException;
import io.todoee.web.reflections.adapters.ParameterAdapterRegistry;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.RoutingContext;

import org.apache.commons.lang3.StringUtils;

public class FormParamInjector implements AnnotatedParamInjector<FormParam> {

	private final ParameterAdapterRegistry adapters;

	public FormParamInjector(ParameterAdapterRegistry adapters) {
		this.adapters = adapters;
	}

	@Override
	public Object resolve(RoutingContext context, FormParam annotation, String paramName, Class<?> resultClass) {
		final String formParamName = StringUtils.isEmpty(annotation.value()) ? paramName : annotation.value();
		final String paramValue = context.request().getFormAttribute(formParamName);

		if (paramValue == null && annotation.required()) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.REQUEST_PARAM_REQUIRED, RestException.class,
//					formParamName);
			WebException e = new WebException(600, WebErrCode.REQUEST_PARAM_REQUIRED);
			e.throwException();
		}

		try {
			return adapters.adaptParam(paramValue, resultClass);
		} catch (IllegalArgumentException iae) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.FORM_ATTRIBUTE_ADAPT_ERROR, RestException.class,
//					formParamName, paramValue);
			WebException e = new WebException(600, WebErrCode.FORM_ATTRIBUTE_ADAPT_ERROR);
			throw e;
		}
	}

}
