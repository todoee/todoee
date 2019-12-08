package io.todoee.wsg.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * 
 * @author James.zhang
 *
 */
public interface AuthRoute {
	void route(Vertx vertx, Router router);
}
