package io.todoee.web.routing.handler.impl;

import io.todoee.web.constants.WebConstant;
import io.todoee.web.exception.WebException;
import io.todoee.web.routing.handler.AjaxLoginHandler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author James.zhang
 */
public class AjaxLoginHandlerImpl implements AjaxLoginHandler {

	private static final Logger log = LoggerFactory
			.getLogger(AjaxLoginHandlerImpl.class);

	private static final String SIMPLE_CAPTCHA = "simpleCaptcha";
	
	private final AuthProvider authProvider;

	private String usernameParam;
	private String passwordParam;
	// private String returnURLParam;
	private String directLoggedInOKURL;

	@Override
	public AjaxLoginHandler setUsernameParam(String usernameParam) {
		this.usernameParam = usernameParam;
		return this;
	}

	@Override
	public AjaxLoginHandler setPasswordParam(String passwordParam) {
		this.passwordParam = passwordParam;
		return this;
	}

	@Override
	public AjaxLoginHandler setReturnURLParam(String returnURLParam) {
		// this.returnURLParam = returnURLParam;
		return this;
	}

	@Override
	public AjaxLoginHandler setDirectLoggedInOKURL(String directLoggedInOKURL) {
		this.directLoggedInOKURL = directLoggedInOKURL;
		return this;
	}

	public AjaxLoginHandlerImpl(AuthProvider authProvider,
			String usernameParam, String passwordParam, String returnURLParam,
			String directLoggedInOKURL) {
		this.authProvider = authProvider;
		this.usernameParam = usernameParam;
		this.passwordParam = passwordParam;
		// this.returnURLParam = returnURLParam;
		this.directLoggedInOKURL = directLoggedInOKURL;
	}

	@Override
	public void handle(RoutingContext context) {
		HttpServerRequest req = context.request();
		if (req.method() != HttpMethod.POST) {
			// context.fail(405); // Must be a POST
			WebException e = new WebException(405, "login must is post");
			context.fail(e);
		} else {
			// if (!req.isExpectMultipart()) {
			// throw new IllegalStateException(
			// "Form body not parsed - do you forget to include a BodyHandler?");
			// }
			// MultiMap params = req.formAttributes();
			// String username = params.get(usernameParam);
			// String password = params.get(passwordParam);
//			JSONObject body = JSON.parseObject(context.getBodyAsString());
//			String username = body.getString(usernameParam);
//			String password = body.getString(passwordParam);

			String username = context.request().formAttributes().get(usernameParam);
			String password = context.request().formAttributes().get(passwordParam);
			String vercode = context.request().formAttributes().get("vercode");
			if (vercode == null) {
				WebException e = new WebException(400,
						"No verify code provided");
				context.fail(e);
				return;
			}
			String code = context.session().get(SIMPLE_CAPTCHA);
			if (!vercode.equals(code)) {
				WebException e = new WebException(400,
						"Verify code incorrect");
				context.fail(e);
				return;
			}
			
			if (username == null || password == null) {
				log.warn("No username or password provided in body - did you forget to include a BodyHandler?");
				// context.fail(400);
				WebException e = new WebException(400,
						"No username or password provided");
				context.fail(e);
			} else {
				Session session = context.session();
				JsonObject authInfo = new JsonObject()
						.put("username", username).put("password", password);
				authProvider
						.authenticate(authInfo, res -> {
							if (res.succeeded()) {
								User user = res.result();
								context.setUser(user);
								if (session != null) {
									// the user has upgraded from
									// unauthenticated to
									// authenticated
									// session should be upgraded as recommended
									// by
									// owasp
								session.regenerateId();
								JSONObject obj = new JSONObject();
								String returnURL = session
										.remove(DEFAULT_RETURN_URL_PARAM);
								if (returnURL != null) {
									context.response().putHeader(WebConstant.TARGET_VIEW,
											returnURL);
									obj.put("action", returnURL);
								} else if (directLoggedInOKURL != null) {
									context.response().putHeader(WebConstant.TARGET_VIEW,
											directLoggedInOKURL);
									obj.put("action", directLoggedInOKURL);
								} else {
									context.response()
											.putHeader(WebConstant.TARGET_VIEW, "");
								}

//								Payload<?> payload = Payload.getInstance();
//								context.response().end(
//										JSON.toJSONString(payload));
								obj.put("status", 0);
								context.response().end(
										JSON.toJSONString(obj));
								
								// String returnURL =
								// session.remove(returnURLParam);

								// if (returnURL != null) {
								// // Now redirect back to the original url
								// doRedirect(req.response(), returnURL);
								// return;
								// }
							}
							// Either no session or no return url
							// if (directLoggedInOKURL != null) {
							// // Redirect to the default logged in OK page -
							// this
							// // would occur
							// // if the user logged in directly at this URL
							// without
							// // being redirected here first from another
							// // url
							// doRedirect(req.response(), directLoggedInOKURL);
							// } else {
							// // Just show a basic page
							// req.response().end(DEFAULT_DIRECT_LOGGED_IN_OK_PAGE);
							// }
						} else {
							// context.fail(403); // Failed login
							WebException e = new WebException(403,
									"username or password incorrect");
							context.fail(e);
						}
					}	);
			}
		}
	}

	// private void doRedirect(HttpServerResponse response, String url) {
	// response.putHeader("location", url).setStatusCode(302).end();
	// }
	//
	// private static final String DEFAULT_DIRECT_LOGGED_IN_OK_PAGE = ""
	// + "<html><body><h1>Login successful</h1></body></html>";
}
