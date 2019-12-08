package io.todoee.httpclient.provider;

import javax.inject.Inject;

import com.google.inject.Provider;

import io.todoee.httpclient.proxy.HttpClientProxyFactory;

/**
 *
 * @author James.zhang
 */
public class HttpClientProxyProvider<P> implements Provider<P> {

    private final Class<P> proxyClass;
    
    @Inject
    private HttpClientProxyFactory httpClientProxyFactory;
    
    public HttpClientProxyProvider(Class<P> proxy) {
        this.proxyClass = proxy;
    }

    @Override
    public P get() {
        P proxy = httpClientProxyFactory.get(this.proxyClass);
        return proxy;
    }

    public static <P> Provider<P> of(Class<P> type) {
		return new HttpClientProxyProvider<P>(type);
	}
}
