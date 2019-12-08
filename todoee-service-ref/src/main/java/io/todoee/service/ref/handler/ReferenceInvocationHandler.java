package io.todoee.service.ref.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import io.todoee.service.ref.Reference;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * 
 * @author James.zhang
 *
 * @param <T>
 */
public class ReferenceInvocationHandler<T> implements InvocationHandler {

	private static Map<String, Reference> serviceMap = new HashMap<>();
    
    public ReferenceInvocationHandler(Vertx vertx, Class<T> clazz) {
        this.initServiceMap(vertx, clazz);
    }
    
    private void initServiceMap(Vertx vertx, Class<T> clazz) {
    	Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Reference service = new Reference(vertx, clazz);
	    	service.setMethod(method);
	    	serviceMap.put(method.getName(), service);
		}
    }
    
	@Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	Future<Object> future = Future.future();
    	String body;
    	if (args.length == 0) {
    		body = "";
		} else {
			body = JSON.toJSONString(args[0]);
		}
    	
    	call(method, body, future);
    	
    	return future;
    }

	private void call(Method method, String body,
			Handler<AsyncResult<Object>> resultHandler) {
		Reference service = serviceMap.get(method.getName());
    	service.call(body).setHandler(reply -> {
    		if (reply.succeeded()) {
    			Class<?> c = service.getParameterizedType();
    			if (c.getName().equals(Void.class.getName())) {
    				resultHandler.handle(Future.succeededFuture());
				} else {
					Object obj = JSON.parseObject(reply.result().body(), c);
	    			resultHandler.handle(Future.succeededFuture(obj));
				}
            } else {
                resultHandler.handle(Future.failedFuture(reply.cause()));
            }
    	});
	}
}
