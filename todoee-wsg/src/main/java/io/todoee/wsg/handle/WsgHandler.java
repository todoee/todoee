package io.todoee.wsg.handle;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.todoee.base.constants.GlobalConstant;
import io.todoee.core.util.EventbusAddress;
import io.todoee.wsg.constants.ContentType;
import io.todoee.wsg.constants.ResultTemplate;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class WsgHandler implements Handler<RoutingContext> {

	@Inject
	private RemoteService service;
	
	@Inject
	private ServiceMapping mapping;
	
	@Override
	public void handle(RoutingContext context) {

		String body = context.getBodyAsString();

		String endpoint = context.request().path();
		
		String traceId = UUID.randomUUID().toString(); 
		ThreadContext.put(GlobalConstant.TRACE_ID, traceId);
		
		ThreadContext.put(GlobalConstant.CURRENT_USER, context.get(GlobalConstant.CURRENT_USER));
		mapping.getService(endpoint).setHandler(handler -> {
			HttpServerResponse response = context.response();
			response.putHeader(HttpHeaderNames.CONTENT_TYPE, ContentType.JSON_CONTENT_TYPE);
			response.putHeader("X-trace-id", traceId);
			
			if (handler.succeeded()) {
				String serviceDefine = handler.result();
				log.debug("success looked up serviceDefine: " + serviceDefine);
				
				if (StringUtils.isEmpty(serviceDefine)) {
					String result = ResultTemplate.fail("gateway-001", "service not found for " + endpoint);
					response.end(result);
					return;
				}
				
				String[] serviceDefines = serviceDefine.split("/");
				if (serviceDefines.length <= 1) {
					String result = ResultTemplate.fail("gateway-002", "service define must contain / for " + endpoint);
					response.end(result);
				}
				
				mapping(response, body, serviceDefines);
			} else {
				context.fail(handler.cause());
			}
		});
	}

	private void mapping(HttpServerResponse response, String body,
			String[] serviceDefines) {
		String clazz = serviceDefines[0];
		String method = serviceDefines[1];
		log.debug("WSG call service " + clazz + "." + method + "() ...");
		
		String address = EventbusAddress.get(clazz);
		
		service.call(address, method, body).setHandler(reply -> {
			if (reply.succeeded()) {
				String resultJson = reply.result().body();
				resultJson = resultJson.equals("null")?"\"void\"":resultJson;
				String result = ResultTemplate.success(resultJson);
				response.end(result);
			} else {
				ReplyException re = (ReplyException)reply.cause();
				log.error("throw exception: " + re.failureCode() + "::" + re.getMessage());
				String result = ResultTemplate.fail(re.failureCode() + "", re.getMessage());
				response.end(result);
			}
		});
	}
}
