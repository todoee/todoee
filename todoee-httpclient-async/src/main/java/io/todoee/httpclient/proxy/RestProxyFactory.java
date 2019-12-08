package io.todoee.httpclient.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.todoee.httpclient.annotation.Host;

/**
 * 
 * @author James.zhang
 *
 */
@Singleton
public class RestProxyFactory {
	
	@Inject
	private InvocationHandler handler;
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> interfaceClass) {
		Host host = interfaceClass.getDeclaredAnnotation(Host.class);
		if (host == null) {
			throw new RuntimeException(interfaceClass + " not found @Host");
		}
		T proxy = (T)Proxy.newProxyInstance(RestProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, handler);
		return proxy;
	}
}
