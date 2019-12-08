package io.todoee.httpclient.module;

import javax.ws.rs.Path;

import com.google.inject.AbstractModule;

import io.todoee.base.utils.PackageScanner;
import io.todoee.httpclient.config.HttpClientConfig;
import io.todoee.httpclient.provider.HttpClientProxyProvider;
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
    	bind(HttpClientConfig.class).asEagerSingleton();
    	for (Class clazz : PackageScanner.scanAnnotation(Path.class)) {
    		log.debug("Found Path Class: " + clazz.getName());
    		bind(clazz).toProvider(HttpClientProxyProvider.of(clazz));
		}
    }
    
}
