package io.todoee.web.utils;

import io.vertx.core.Vertx;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;

/**
 * 
 * @author James.zhang
 *
 */
public class GlobalStore {
	private static final String GLOBAL_STORE_KEY = "global_store_key";
	
	public static LocalMap<String, String> data(Vertx vertx) {
		SharedData sd = vertx.sharedData();
		return sd.getLocalMap(GLOBAL_STORE_KEY);
	}
}
