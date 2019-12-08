package io.todoee.core.loader;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import io.todoee.core.context.IocContext;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class VerticleLoader<T> {

	protected abstract boolean worker();
	protected abstract int instances();
	
	protected Vertx vertx() {
		return IocContext.getBean(Vertx.class);
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> type() {
		Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return tClass;
	}
	
	public List<Future<Void>> load() {
		List<Future<Void>> futures = new ArrayList<>();
		ServiceLoader<T> loaders = ServiceLoader.load(type());
		loaders.forEach(verticle -> {
			futures.add(createDeployFuture(verticle.getClass().getName(), instances(), worker()));
		});
		return futures;
	}
	
	private Future<Void> createDeployFuture(String verticleName, int instances, boolean worker) {
		Future<Void> future = Future.future();
		log.debug("deploy verticle: " + verticleName);
		vertx().deployVerticle(verticleName,
				new DeploymentOptions().setInstances(instances).setWorker(worker),
				res -> {
					if (res.succeeded()) {
						log.info(verticleName + " deploy success, deployment id: " + res.result());
						future.complete();
					} else {
						future.fail(res.cause());
					}
				});
		return future;
	}
}
