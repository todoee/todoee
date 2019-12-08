package io.todoee.web.routing.handler;

import io.vertx.ext.web.handler.AuthHandler;
import io.todoee.web.routing.handler.impl.RedirectAuthHandlerImpl;
import io.vertx.ext.auth.AuthProvider;

/**
 * An auth handler that's used to handle auth by redirecting user to a custom
 * login page.
 *
 * @author James.zhang
 */
public interface RedirectAuthHandler extends AuthHandler {

	/**
	 * Default path the user will be redirected to
	 */
	String DEFAULT_LOGIN_REDIRECT_URL = "/loginpage";

	/**
	 * Default name of param used to store return url information in session
	 */
	String DEFAULT_RETURN_URL_PARAM = "return_url";

	/**
	 * Create a handler
	 *
	 * @param authProvider
	 *            the auth service to use
	 * @return the handler
	 */
	static AuthHandler create(AuthProvider authProvider) {
		return new RedirectAuthHandlerImpl(authProvider,
				DEFAULT_LOGIN_REDIRECT_URL, DEFAULT_RETURN_URL_PARAM);
	}

	/**
	 * Create a handler
	 *
	 * @param authProvider
	 *            the auth service to use
	 * @param loginRedirectURL
	 *            the url to redirect the user to
	 * @return the handler
	 */
	static AuthHandler create(AuthProvider authProvider, String loginRedirectURL) {
		return new RedirectAuthHandlerImpl(authProvider, loginRedirectURL,
				DEFAULT_RETURN_URL_PARAM);
	}

	/**
	 * Create a handler
	 *
	 * @param authProvider
	 *            the auth service to use
	 * @param loginRedirectURL
	 *            the url to redirect the user to
	 * @param returnURLParam
	 *            the name of param used to store return url information in
	 *            session
	 * @return the handler
	 */
	static AuthHandler create(AuthProvider authProvider,
			String loginRedirectURL, String returnURLParam) {
		return new RedirectAuthHandlerImpl(authProvider, loginRedirectURL,
				returnURLParam);
	}
}
