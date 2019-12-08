package io.todoee.httpclient.provider;

import javax.inject.Inject;

import com.google.inject.Provider;

import io.todoee.httpclient.proxy.RestProxyFactory;

/**
 *
 * @author James.zhang
 */
public class RestProxyProvider<P> implements Provider<P> {

    private final Class<P> proxyClass;
    
    @Inject
    private RestProxyFactory restProxyFactory;
    
    public RestProxyProvider(Class<P> proxy) {
        this.proxyClass = proxy;
    }

    @Override
    public P get() {
        P proxy = restProxyFactory.get(this.proxyClass);
        return proxy;
    }

    public static <P> Provider<P> of(Class<P> type) {
		return new RestProxyProvider<P>(type);
	}
}
