package io.todoee.service.ref.provider;

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.inject.Inject;

import com.google.inject.Provider;

import io.todoee.service.ref.proxy.ProxyFactory;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author James.zhang
 */
@Slf4j
public class ReferenceProxyProvider<P> implements Provider<P> {

    private final Class<P> proxyClass;
    
    private static ProxyFactory proxyFactory;
    
    @Inject
    private Vertx vertx;
    
    static {
    	ServiceLoader<ProxyFactory> loaders = ServiceLoader.load(ProxyFactory.class);
    	Iterator<ProxyFactory> iterator = loaders.iterator();
    	if (iterator.hasNext()) {
    		proxyFactory = loaders.iterator().next();
    		log.debug("load proxy factory: " + proxyFactory);
		}
    }
    
    public ReferenceProxyProvider(Class<P> proxy) {
        this.proxyClass = proxy;
    }

    @Override
    public P get() {
        P proxy = proxyFactory.get(vertx, this.proxyClass);
        return proxy;
    }

    public static <P> Provider<P> of(Class<P> type) {
		return new ReferenceProxyProvider<P>(type);
	}
}
