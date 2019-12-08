package io.todoee.web.routing.handler.impl;

import lombok.extern.slf4j.Slf4j;
import io.todoee.web.constants.WebConstant;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;
import io.vertx.ext.web.handler.impl.HttpStatusException;

/**
 * @author James.zhang
 */
@Slf4j
public class RedirectAuthHandlerImpl extends AuthHandlerImpl {

	private final String loginRedirectURL;
	private final String returnURLParam;

	public RedirectAuthHandlerImpl(AuthProvider authProvider,
			String loginRedirectURL, String returnURLParam) {
		super(authProvider);
		this.loginRedirectURL = loginRedirectURL;
		this.returnURLParam = returnURLParam;
	}

	@Override
	public void parseCredentials(RoutingContext context,
			Handler<AsyncResult<JsonObject>> handler) {
		Session session = context.session();
		if (session != null) {
			// Now redirect to the login url - we'll get redirected back here
			// after successful login
			String returnURL;
			if (context.request().getHeader("x-requested-with") != null
					&& context.request().getHeader("x-requested-with")
							.equalsIgnoreCase("XMLHttpRequest")) {
				returnURL = context.request().getHeader("REFERER");
				log.debug("redirect ajax request, REFERER url: " + returnURL);
				
				session.put(returnURLParam, returnURL);
				context.response().putHeader(WebConstant.TARGET_VIEW, loginRedirectURL)
						.setStatusCode(302).end();
			} else {
				returnURL = context.request().uri();
				log.debug("redirect view request, return url: " + returnURL);
				session.put(returnURLParam, context.request().uri());
				handler.handle(Future.failedFuture(new HttpStatusException(302,
						loginRedirectURL)));
			}
		} else {
			handler.handle(Future
					.failedFuture("No session - did you forget to include a SessionHandler?"));
		}
	}
}
