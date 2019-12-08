package io.todoee.wsg.limit;

import java.util.concurrent.TimeUnit;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.todoee.wsg.constants.ContentType;
import io.todoee.wsg.constants.ResultTemplate;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.RoutingContext;

/**
 * 
 * @author James.zhang
 *
 */
public class RateLimitationHandler implements Handler<RoutingContext> {

	private RateLimit rateLimit;

	private Integer count;
	private Integer interval;
	
	public RateLimitationHandler() {
		this.rateLimit = new RateLimit(count, interval, TimeUnit.SECONDS);
	}

	@Override
	public void handle(RoutingContext context) {
		Vertx vertx = context.vertx();
		LocalMap<Object, Object> rateLimitations = vertx.sharedData()
				.getLocalMap("gateway.rateLimitation");
		String clientIp = context.request().remoteAddress().host();
		JsonObject json = (JsonObject) rateLimitations.get(clientIp);
		ClientAccesses accesses;
		if (json == null) {
			accesses = new ClientAccesses();
		} else {
			accesses = ClientAccesses.fromJsonObject(json);
		}
		accesses.newAccess();
		rateLimitations.put(clientIp, accesses.toJsonObject());
		if (accesses.isOverLimit(rateLimit)) {
			HttpServerResponse response = context.response();
			response.putHeader(HttpHeaderNames.CONTENT_TYPE, ContentType.JSON_CONTENT_TYPE);
			String result = ResultTemplate.fail("420", "Access exceeded maximum threshold");
			response.end(result);
		} else {
			context.next();
		}
	}

}
