package io.todoee.httpclient.module;

import java.lang.reflect.InvocationHandler;

import com.google.inject.AbstractModule;

import io.todoee.base.utils.PackageScanner;
import io.todoee.httpclient.annotation.Host;
import io.todoee.httpclient.provider.RestProxyProvider;
import io.todoee.httpclient.proxy.RestInvocationHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class HttpClientModule extends AbstractModule {
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected void configure() {
    	bind(InvocationHandler.class).to(RestInvocationHandler.class).asEagerSingleton();
    	for (Class clazz : PackageScanner.scanAnnotation(Host.class)) {
    		log.debug("Found Host Class: " + clazz.getName());
    		bind(clazz).toProvider(RestProxyProvider.of(clazz)).asEagerSingleton();
		}
    }
    
}
