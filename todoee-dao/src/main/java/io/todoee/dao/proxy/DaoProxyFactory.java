package io.todoee.dao.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.todoee.dao.annotation.Table;

/**
 * 
 * @author James.zhang
 *
 */
@Singleton
public class DaoProxyFactory {
	
	@Inject
	private InvocationHandler handler;
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> interfaceClass) {
		Table table = interfaceClass.getDeclaredAnnotation(Table.class);
		if (table == null) {
			throw new RuntimeException(interfaceClass + " not found @Table");
		}
		T proxy = (T)Proxy.newProxyInstance(DaoProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, handler);
		return proxy;
	}
}
