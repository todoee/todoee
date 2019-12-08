package io.todoee.wsg.verticle;

import io.todoee.core.context.IocContext;
import io.todoee.core.verticle.WsgAbstractVerticle;
import io.todoee.wsg.config.WsgConfig;
import io.todoee.wsg.handle.WsgHandler;
import io.todoee.wsg.route.AuthRoute;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class WsgVerticle extends WsgAbstractVerticle {

	private HttpServer server;

	private Router router;

	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		router = router(vertx);
		server = server(vertx);
	}

	private Router router(Vertx vertx) {
		return Router.router(vertx);
	}

	private HttpServer server(Vertx vertx) {
		HttpServerOptions options = new HttpServerOptions();
		return vertx.createHttpServer(options);
	}

	@Override
	public void start(Future<Void> startFuture) {

		addLoggerRoute();

		addBodyRoute();

		addAuthRoute();

		addWsgRoute();

		startHttpServer(startFuture);
	}

	private void addAuthRoute() {
		AuthRoute authRouteDefinition = IocContext
				.getBean(AuthRoute.class);
		if (authRouteDefinition != null) {
			authRouteDefinition.route(getVertx(), router);
			log.debug("vertx add auth route: " + authRouteDefinition.getClass());
		} else {
			log.warn("not found auth route");
		}
	}

	private void addWsgRoute() {
//		router.post("/*").handler(RateLimitationHandler.create());
		WsgHandler wsgHandler = IocContext.getBean(WsgHandler.class);
		router.post("/*").handler(wsgHandler);
	}

	private void addLoggerRoute() {
		router.route().handler(LoggerHandler.create(LoggerFormat.SHORT));
	}

	private void addBodyRoute() {
		BodyHandler bodyHandler = BodyHandler.create();
		router.put().handler(bodyHandler);
		router.post().handler(bodyHandler);
	}

	private void startHttpServer(Future<Void> startFuture) {
		WsgConfig config = IocContext.getBean(WsgConfig.class);
		server.requestHandler(router)
				.listen(config.getPort(),
						result -> {
							if (result.succeeded()) {
								log.info("WSG startup on port " + config.getPort());
								startFuture.complete();
							} else {
								log.error("WSG startup Fail");
								startFuture.fail(result.cause());
							}
						});
	}
}
