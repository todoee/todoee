package io.todoee.wsg.handle;

import javax.inject.Inject;

import org.apache.logging.log4j.ThreadContext;

import io.todoee.base.constants.GlobalConstant;
import io.todoee.wsg.constants.ResultTemplate;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class RemoteService {

	@Inject
	private Vertx vertx;

	public Future<Message<String>> call(String address, String method, String body) {
		Future<Message<String>> future = Future.future();
		JsonObject msg = new JsonObject();
		msg.put("body", body);
		DeliveryOptions deliveryOptions = new DeliveryOptions();
	    deliveryOptions.addHeader("action", method);
	    String traceId = ThreadContext.get(GlobalConstant.TRACE_ID);
	    if (traceId != null) {
	    	deliveryOptions.addHeader(GlobalConstant.TRACE_ID, traceId);
		}
	    
	    String currentUser = ThreadContext.get(GlobalConstant.CURRENT_USER);
	    if (currentUser != null) {
	    	deliveryOptions.addHeader(GlobalConstant.CURRENT_USER, currentUser);
	    }
	    
	    vertx.eventBus().<String>send(address, msg, deliveryOptions, future);
		
		return future;
	}
	
	public void call2(HttpServerResponse response, String address, String method, String body) {
		JsonObject msg = new JsonObject();
		msg.put("body", body);
		DeliveryOptions deliveryOptions = new DeliveryOptions();
		deliveryOptions.addHeader("action", method);
		String traceId = ThreadContext.get(GlobalConstant.TRACE_ID);
		if (traceId != null) {
			deliveryOptions.addHeader(GlobalConstant.TRACE_ID, traceId);
		}
		
		String currentUser = ThreadContext.get(GlobalConstant.CURRENT_USER);
		if (currentUser != null) {
			deliveryOptions.addHeader(GlobalConstant.CURRENT_USER, currentUser);
		}
		
		EventBus eventBus = vertx.eventBus();
		eventBus.<String>send(address, msg, deliveryOptions, reply -> {
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
