package io.todoee.web.routing.handler;

import io.todoee.web.routing.handler.impl.AjaxLoginHandlerImpl;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.auth.AuthProvider;

/**
 * Handler that handles login from a form on a custom login page.
 * <p>
 * Used in conjunction with the {@link RedirectAuthHandler}.
 *
 * @author James.zhang
 */
public interface AjaxLoginHandler extends Handler<RoutingContext> {

	/**
	 * The default value of the form attribute which will contain the username
	 */
	String DEFAULT_USERNAME_PARAM = "username";

	/**
	 * The default value of the form attribute which will contain the password
	 */
	String DEFAULT_PASSWORD_PARAM = "password";

	/**
	 * The default value of the form attribute which will contain the return url
	 */
	String DEFAULT_RETURN_URL_PARAM = "return_url";

	/**
	 * Create a handler
	 *
	 * @param authProvider
	 *            the auth service to use
	 * @return the handler
	 */
	static AjaxLoginHandler create(AuthProvider authProvider) {
		return new AjaxLoginHandlerImpl(authProvider, DEFAULT_USERNAME_PARAM,
				DEFAULT_PASSWORD_PARAM, DEFAULT_RETURN_URL_PARAM, null);
	}

	/**
	 * Create a handler
	 *
	 * @param authProvider
	 *            the auth service to use
	 * @param usernameParam
	 *            the value of the form attribute which will contain the
	 *            username
	 * @param passwordParam
	 *            the value of the form attribute which will contain the
	 *            password
	 * @param returnURLParam
	 *            the value of the session attribute which will contain the
	 *            return url
	 * @param directLoggedInOKURL
	 *            a url to redirect to if the user logs in directly at the url
	 *            of the form login handler without being redirected here first
	 *
	 * @return the handler
	 */
	static AjaxLoginHandler create(AuthProvider authProvider,
			String usernameParam, String passwordParam, String returnURLParam,
			String directLoggedInOKURL) {
		return new AjaxLoginHandlerImpl(authProvider, usernameParam,
				passwordParam, returnURLParam, directLoggedInOKURL);
	}

	/**
	 * Set the name of the form param used to submit the username
	 * 
	 * @param usernameParam
	 *            the name of the param
	 * @return a reference to this for a fluent API
	 */
	AjaxLoginHandler setUsernameParam(String usernameParam);

	/**
	 * Set the name of the form param used to submit the password
	 * 
	 * @param passwordParam
	 *            the name of the param
	 * @return a reference to this for a fluent API
	 */
	AjaxLoginHandler setPasswordParam(String passwordParam);

	/**
	 * Set the name of the session attrioute used to specify the return url
	 * 
	 * @param returnURLParam
	 *            the name of the param
	 * @return a reference to this for a fluent API
	 */
	AjaxLoginHandler setReturnURLParam(String returnURLParam);

	/**
	 * Set the url to redirect to if the user logs in directly at the url of the
	 * form login handler without being redirected here first
	 * 
	 * @param directLoggedInOKURL
	 *            the URL to redirect to
	 * @return a reference to this for a fluent API
	 */
	AjaxLoginHandler setDirectLoggedInOKURL(String directLoggedInOKURL);
}
