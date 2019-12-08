package io.todoee.core.boot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import io.todoee.core.context.IocContext;
import io.todoee.core.loader.VerticleLoader;
import io.todoee.core.spi.ClusterFactory;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class Launcher {

	public static void run() {
		vertxDeploy(vertx -> {
			deployAll(vertx);
		});
	}

	private static void vertxDeploy(Consumer<Vertx> deploier) {
		BootConfig config = IocContext.getBean(BootConfig.class);
		VertxOptions vertxOptions = new VertxOptions();
		if (config.getWsgInstances() > VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE) {
			vertxOptions.setEventLoopPoolSize(config.getWsgInstances());
		}
		if (config.getServiceInstances() > VertxOptions.DEFAULT_WORKER_POOL_SIZE) {
			vertxOptions.setWorkerPoolSize(config.getServiceInstances());
		}
		if (config.getWebInstances() > VertxOptions.DEFAULT_WORKER_POOL_SIZE) {
			vertxOptions.setWorkerPoolSize(config.getServiceInstances());
		}
		
		ServiceLoader<ClusterFactory> clusterModules = ServiceLoader.load(ClusterFactory.class);
		Iterator<ClusterFactory> iterator = clusterModules.iterator();
		ClusterFactory clusterModule;
		if (iterator.hasNext()) {
			clusterModule = iterator.next();
			IocContext.createChildInjector(clusterModule.get());
			ClusterManager mgr = IocContext.getBean(ClusterManager.class);
			vertxOptions.setClusterManager(mgr)
					.setEventBusOptions(new EventBusOptions().setHost(config.getServiceHost()).setReconnectAttempts(2));
			Vertx.clusteredVertx(vertxOptions, res -> {
				if (res.succeeded()) {
					Vertx vertx = res.result();
					deploier.accept(vertx);
					log.info("create cluster vertx instance succeeded");
				} else {
					log.error("create cluster vertx instance fail", res.cause());
				}
			});
		} else {
			Vertx vertx = Vertx.vertx(vertxOptions);
			deploier.accept(vertx);
			log.info("create local vertx instance succeeded");
		}
	}

	private static void deployAll(Vertx vertx) {
		Future<Void> rootFuture = Future.future();
		IocContext.init(vertx, rootFuture);
		rootFuture.compose(v -> {
			Future<Void> future = Future.future();
			CompositeFuture.all(getFutures(vertx)).setHandler(ar -> {
				if (ar.succeeded()) {
					log.info("All verticles deploy successfully");
					future.complete();
				} else {
					future.fail(ar.cause());
				}
			});
			return future;
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<Future> getFutures(Vertx vertx) {
		List<Future> futures = new ArrayList<>();
		ServiceLoader<VerticleLoader> verticleLoaders = ServiceLoader.load(VerticleLoader.class);
		verticleLoaders.forEach(loader -> {
			futures.addAll(loader.load());
		});
		return futures;
	}

}
