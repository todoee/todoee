package io.todoee.web.routing;

import java.util.Set;

import io.todoee.base.utils.PackageScanner;
import io.todoee.web.annotations.RequestMapping;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class RouteFactory {

	private Router router;

	private RouteFactory(Router router) {
		this.router = router;
	}

	public static RouteFactory getInstance(Router router) {
		return new RouteFactory(router);
	}

	public void build() {
		Set<Class<?>> scanAnnotation = PackageScanner.scanAnnotation(RequestMapping.class);
		
		scanAnnotation.forEach(clazz -> {
			log.debug("build web route, loaded request mapping class: "
					+ clazz.getName());
			RouteBuilder.build(clazz, router);
		});
	}
}
