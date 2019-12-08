package io.todoee.core.context;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

import io.todoee.core.spi.ReferenceModule;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class IocContext {

	private final static String PLATFORM_NAME = "IOC Context";

	private static Injector injector;
	
	static {
		injector = Guice.createInjector();
	}
	
	public static void init(Vertx vertx, Handler<AsyncResult<Void>> resultHandler) {
		vertx.executeBlocking(future -> {
			log.info(PLATFORM_NAME + " Init...");
			long beginTime = System.currentTimeMillis();
			build(vertx);
			future.complete(System.currentTimeMillis() - beginTime);
		}, res -> {
			if (res.succeeded()) {
				log.info(PLATFORM_NAME + " Inited in " + res.result() + "ms");
				resultHandler.handle(Future.succeededFuture());
			} else {
				log.error(PLATFORM_NAME + " Init fail", res.cause());
				resultHandler.handle(Future.failedFuture(res.cause()));
			}
		});
	}

	public static void createChildInjector(Module... modules) {
		injector = injector.createChildInjector(modules);
	}
	
	public static void build(Vertx vertx) {
		createChildInjector(binder -> {
			binder.bind(Vertx.class).toInstance(vertx);
		});
		
		createChildInjector(binder -> {
			ServiceLoader<Module> loaders = ServiceLoader.load(Module.class);
			loaders.forEach(module -> {
				log.info("install module: " + module.getClass().getName());
				binder.install(module);
			});
		});
		
		ServiceLoader<ReferenceModule> clusterModules = ServiceLoader.load(ReferenceModule.class);
		Iterator<ReferenceModule> iterator = clusterModules.iterator();
		ReferenceModule refModule;
		if (iterator.hasNext()) {
			refModule = iterator.next();
			createChildInjector(refModule.get());
		}
	}
	
	public static void inject(Object instance) {
		injector.injectMembers(instance);
	}
	
	public static Injector getInjector() {
		return injector;
	}
	
	public static <T> T getBean(Class<T> clazz, String beanName) {
		try {
			T t = injector.getInstance(Key.get(clazz, Names.named(beanName)));
			return t;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static <T> T getBean(Class<T> clazz) {
		try {
			T t = injector.getInstance(clazz);
			return t;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static List<Object> getBeansByAnnotation(Class<? extends Annotation> anno) {
		List<Object> beanList = new ArrayList<>();
		Map<Key<?>, Binding<?>> map = injector.getAllBindings();
		map.entrySet().forEach(entry -> {
			Class<?> clazz = entry.getValue().getKey().getTypeLiteral().getRawType();
			if(clazz.isAnnotationPresent(anno)){
				log.debug("Annotation " + anno.getName() + " found class:" + clazz.getName());
				beanList.add(injector.getInstance(clazz));
			}
		});
		return beanList;
	}
		
}
