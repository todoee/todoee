package io.todoee.web.verticle;

import io.todoee.core.context.IocContext;
import io.todoee.web.auth.AuthRealm;
import io.todoee.web.auth.SkyAuth;
import io.todoee.web.config.WebConfig;
import io.todoee.web.routing.RouteFactory;
import io.todoee.web.routing.handler.AjaxLoginHandler;
import io.todoee.web.routing.handler.RedirectAuthHandler;
import io.todoee.web.routing.handler.impl.DefaultErrorHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 * 
 */
@Slf4j
public class WebVerticle extends AbstractVerticle {

	private WebConfig config;

	private Router router;

	private HttpServer server;

	@Override
	public void init(Vertx vertx, Context context) {
		config = IocContext.getBean(WebConfig.class);
		super.init(vertx, context);
		router = Router.router(vertx);
		server = server(vertx);
	}

	private HttpServer server(Vertx vertx) {
		HttpServerOptions options = new HttpServerOptions();
		options.setCompressionSupported(true);
		return vertx.createHttpServer(options);
	}

	@Override
	public void start() throws Exception {
		addStaticRoute();

		router.route().handler(LoggerHandler.create(LoggerFormat.SHORT));
		router.route().failureHandler(new DefaultErrorHandler());

		addBodyRoute();

		if (config.getAuth()) {
			addSessionRoute();
		}

		RouteFactory.getInstance(router).build();

		server.requestHandler(router).listen(
				config.getPort(),
				ar -> {
					log.debug("Todoee Web startup on port "
							+ ar.result().actualPort());
				});
	}

	private void addStaticRoute() {
		router.get("/static/*").handler(
				StaticHandler.create().setWebRoot("META-INF/resources/static")
				.setDirectoryListing(false).setCachingEnabled(config.getStaticCache()));
	}

	private void addSessionRoute() {
		SessionHandler sessionHandler = SessionHandler
				.create(ClusteredSessionStore.create(vertx));
		if (vertx.isClustered()) {
			sessionHandler = SessionHandler.create(ClusteredSessionStore
					.create(vertx));
		} else {
			sessionHandler = SessionHandler.create(LocalSessionStore
					.create(vertx));
		}
		sessionHandler.setSessionTimeout(config.getSessionTimeout());
		router.route().handler(CookieHandler.create());
		
		AuthProvider authProvider = SkyAuth.create(vertx,
				IocContext.getBean(AuthRealm.class));
		router.route().handler(sessionHandler.setAuthProvider(authProvider));

		AuthHandler redirectHandler = RedirectAuthHandler.create(authProvider,
				config.getLogin());
		router.route().pathRegex("^(/(" + config.getPrivatePrefix() + ")).*$").handler(redirectHandler);

		router.post("/login").handler(
				AjaxLoginHandler.create(authProvider).setDirectLoggedInOKURL(
						config.getHome())
						.setUsernameParam(config.getUsernameKey())
						.setPasswordParam(config.getPasswordKey()));
		router.get("/logout").handler(
				context -> {
					context.clearUser();
					context.response().putHeader("location", config.getLogin())
							.setStatusCode(302).end();
				});
	}

	private void addBodyRoute() {
		BodyHandler bodyHandler = BodyHandler.create();
		router.put().handler(bodyHandler);
		router.post().handler(bodyHandler);
	}
}
