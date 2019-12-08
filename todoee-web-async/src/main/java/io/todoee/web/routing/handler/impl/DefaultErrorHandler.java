package io.todoee.web.routing.handler.impl;

import com.alibaba.fastjson.JSON;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.todoee.core.exception.ServiceException;
import io.todoee.web.exception.WebErrCode;
import io.todoee.web.exception.WebException;
import io.todoee.web.result.Payload;
import io.todoee.web.routing.handler.ErrorHandler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class DefaultErrorHandler implements ErrorHandler {

	@Override
	public void handle(RoutingContext context) {
		Throwable e = context.failure();
		e = e.getCause() != null?e.getCause():e;
		log.error("call service error", e);
		
		Payload payload;
		if (e instanceof ServiceException) {
			payload = getCommonExceptionPayload(e);
		} else {
			payload = getOtherExceptionPayload();
		}
		context.response().putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		context.response().end(JSON.toJSONString(payload));
	}

	private Payload getCommonExceptionPayload(Throwable e) {
		ServiceException commonException = (ServiceException) e;
		Payload payload = buildErrPayload(commonException);
		return payload;
	}

	private Payload getOtherExceptionPayload() {
		WebException restException = new WebException(500, WebErrCode.INTERNAL_SERVER_ERROR);
		Payload payload = buildErrPayload(restException);
		return payload;
	}

	private Payload buildErrPayload(ServiceException e) {
		Integer errCode = e.failureCode();
		String message = e.getMessage();
		
		log.debug("error handler code:" + errCode);
		log.debug("error handler message:" + message);
		
		Payload payload = Payload.getInstance();
		payload.setStatus(errCode);
		payload.setCode(errCode);
		payload.setMessage(message);
		payload.setMsg(message);
		return payload;
	}
}
