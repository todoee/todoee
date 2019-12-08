package io.todoee.service.ref.proxy;

import io.todoee.service.ref.handler.ReferenceInvocationHandler;
import io.vertx.core.Vertx;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 
 * @author James.zhang
 *
 */
public class ReferenceProxyFactory implements ProxyFactory {
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Vertx vertx, Class<T> interfaceClass) {
		InvocationHandler handler = new ReferenceInvocationHandler<T>(vertx, interfaceClass);
		T proxy = (T)Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class[]{interfaceClass}, handler);
		return proxy;
	}
}
