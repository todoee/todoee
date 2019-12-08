package io.todoee.wsg.auth;

import javax.inject.Singleton;

import io.todoee.core.context.IocContext;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

/**
 * 
 * @author James.zhang
 *
 */
@Singleton
public class SharedDataHolder {

	private static final String DATA_MAP = "data-map";

	private SharedData sharedData;

	public SharedDataHolder() {
		Vertx vertx = IocContext.getBean(Vertx.class);
		sharedData = vertx.sharedData();
	}

	public Future<String> get(String key) {
		Future<String> future = Future.future();
		map().compose(map -> Future.<String>future(v -> map.get(key, future))).setHandler(future);
		return future;
	}

	public Future<Void> set(String key, String value, long expire) {
		Future<Void> future = Future.future();
		map().compose(map -> Future.<Void>future(v -> map.put(key, value, expire * 1000, future))).setHandler(future);
		return future;
	}

	public Future<String> del(String key) {
		Future<String> future = Future.future();
		map().compose(map -> Future.<String>future(v -> map.remove(key, future))).setHandler(future);
		return future;
	}

	private Future<AsyncMap<String, String>> map() {
		Future<AsyncMap<String, String>> future = Future.future();
		sharedData.<String, String>getAsyncMap(DATA_MAP, future);
		return future;
	}
}
