package io.todoee.service.ref.proxy;

import io.vertx.core.Vertx;

/**
 * 
 * @author James.zhang
 *
 */
public interface ProxyFactory {
	<T> T get(Vertx vertx, Class<T> interfaceClass);
}
