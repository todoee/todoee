package io.todoee.dao;

import javax.inject.Inject;

import com.google.inject.Provider;

import io.todoee.dao.proxy.DaoProxyFactory;

/**
 *
 * @author James.zhang
 */
public class DaoProxyProvider<P> implements Provider<P> {

    private final Class<P> proxyClass;
    
    @Inject
    private DaoProxyFactory daoProxyFactory;
    
    public DaoProxyProvider(Class<P> proxy) {
        this.proxyClass = proxy;
    }

    @Override
    public P get() {
        P proxy = daoProxyFactory.get(this.proxyClass);
        return proxy;
    }

    public static <P> Provider<P> of(Class<P> type) {
		return new DaoProxyProvider<P>(type);
	}
}
