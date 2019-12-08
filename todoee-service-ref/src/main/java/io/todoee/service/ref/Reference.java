package io.todoee.service.ref;

import io.todoee.base.constants.GlobalConstant;
import io.todoee.core.util.EventbusAddress;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.logging.log4j.ThreadContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class Reference {

	private Method method;

	private Vertx vertx;

	private Class<?> clazz;
	
	private Class<?> parameterizedType;
	
	public Reference(Vertx vertx, Class<?> clazz) {
		this.vertx = vertx;
		this.clazz = clazz;
	}

	public Future<Message<String>> call(String body) {
		Future<Message<String>> future = Future.future();
		JsonObject msg = new JsonObject();
		msg.put("body", body);
		DeliveryOptions deliveryOptions = new DeliveryOptions();
	    deliveryOptions.addHeader("action", method.getName());
	    deliveryOptions.addHeader(GlobalConstant.TRACE_ID, ThreadContext.get(GlobalConstant.TRACE_ID));
	    deliveryOptions.addHeader(GlobalConstant.CURRENT_USER, ThreadContext.get(GlobalConstant.CURRENT_USER));
	    
	    String address = EventbusAddress.get(clazz.getName());
	    vertx.eventBus().<String>send(address, msg, deliveryOptions, future);
		
		return future;
	}

	public void setMethod(Method method) {
		this.method = method;
		setParameterizedType(method);
	}
	
	private void setParameterizedType(Method method) {
		Type type = method.getGenericReturnType();
		setParameterizedType(type);
	}

	private void setParameterizedType(Type t) {
		Type type = ((ParameterizedType) t).getActualTypeArguments()[0];
		String typeName = type.getTypeName();
		try {
			parameterizedType = Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			log.error("class: " + parameterizedType + " is not found");
		}
		log.debug(method.getName() + "()'s param is parameterized: " + parameterizedType);
	}
	
	public Class<?> getParameterizedType() {
		return parameterizedType;
	}
}
