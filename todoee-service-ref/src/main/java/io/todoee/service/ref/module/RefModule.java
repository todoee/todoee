package io.todoee.service.ref.module;

import com.google.inject.AbstractModule;

import io.todoee.core.context.IocContext;
import io.todoee.service.ref.holder.ReferenceHolder;
import io.todoee.service.ref.provider.ReferenceProxyProvider;

/**
 * 
 * @author James.zhang
 *
 */
public class RefModule extends AbstractModule {
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected void configure() {
    	for (Class clazz : ReferenceHolder.references()) {
    		Object obj = IocContext.getBean(clazz);
    		if (obj == null) {
    			bind(clazz).toProvider(ReferenceProxyProvider.of(clazz)).asEagerSingleton();
			}
		}
    }
}
