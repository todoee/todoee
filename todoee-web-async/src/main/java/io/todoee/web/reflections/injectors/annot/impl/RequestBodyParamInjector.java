package io.todoee.web.reflections.injectors.annot.impl;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.todoee.web.annotations.params.RequestBody;
import io.todoee.web.exception.WebErrCode;
import io.todoee.web.exception.WebException;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjector;
import io.vertx.ext.web.RoutingContext;

import com.alibaba.fastjson.JSON;

public class RequestBodyParamInjector implements AnnotatedParamInjector<RequestBody> {

	@Override
	public Object resolve(RoutingContext context, RequestBody annotation, String paramName, Class<?> resultClass) {
		String body = context.getBodyAsString();
		if (resultClass.equals(String.class)) {
			return body;
		}
		String contentType = context.request().getHeader(HttpHeaderNames.CONTENT_TYPE);
		if (contentType == null) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.CONTENT_TYPE_EMPTY, RestException.class);
			WebException e = new WebException(600, WebErrCode.CONTENT_TYPE_EMPTY);
			e.throwException();
		}
		if (!contentType.contains(HttpHeaderValues.APPLICATION_JSON)) {
//			RestException e = ExceptionFactory.getInstance(RestErrCode.CONTENT_TYPE_INVALID, RestException.class,
//					contentType);
			WebException e = new WebException(600, WebErrCode.CONTENT_TYPE_INVALID);
			e.throwException();
		}
		return JSON.parseObject(body, resultClass);
	}

}
