package io.todoee.service.export;

import io.todoee.core.context.IocContext;
import io.todoee.core.exception.ServiceException;
import io.todoee.core.exception.ServiceExceptionMessageCodec;
import io.todoee.core.util.EventbusAddress;
import io.todoee.service.proxy.GenericServiceBinder;
import io.vertx.core.Vertx;

/**
 * 
 * @author James.zhang
 *
 */
public class ServiceRegist {
	
	private static Vertx vertx;
	
	static {
		vertx = IocContext.getBean(Vertx.class);
		vertx.eventBus().registerDefaultCodec(ServiceException.class,
				new ServiceExceptionMessageCodec());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void addService(Class<?> service, Object serviceImpl) {
		IocContext.inject(serviceImpl);
		GenericServiceBinder binder = new GenericServiceBinder(vertx)
				.setAddress(EventbusAddress.get(service.getName()));
		binder.register((Class<T>)service, (T)serviceImpl);
	}
	
}
