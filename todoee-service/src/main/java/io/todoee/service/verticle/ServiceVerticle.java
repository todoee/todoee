package io.todoee.service.verticle;

import java.util.Set;

import io.todoee.base.utils.PackageScanner;
import io.todoee.core.verticle.ServiceAbstractVerticle;
import io.todoee.service.annotation.Export;
import io.todoee.service.export.ServiceRegist;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class ServiceVerticle extends ServiceAbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) {
		Set<Class<?>> exports = PackageScanner.scanAnnotation(Export.class);
		for (Class<?> clazz : exports) {
			Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces.length == 0) {
				throw new RuntimeException("Impl class " + clazz.getName() + " not found interface");
			} else if (interfaces.length > 1) {
				throw new RuntimeException("Impl class " + clazz.getName() + " interface count > 1");
			} else {
				log.info("export service: " + interfaces[0].getName());
				try {
					ServiceRegist.addService(interfaces[0], clazz.newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		startFuture.complete();
	}
}
