package io.todoee.wsg.auth;

import javax.inject.Inject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.todoee.base.constants.GlobalConstant;
import io.todoee.base.utils.ObjUtil;
import io.todoee.core.context.IocContext;
import io.todoee.core.exception.ExceptionFactory;
import io.todoee.core.exception.ServiceException;
import io.todoee.wsg.config.WsgConfig;
import io.todoee.wsg.constants.ResultTemplate;
import io.todoee.wsg.route.AuthRoute;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class CommonAuthRoute implements AuthRoute {

	private AbstractAuthentication authentication;

	private static final String HEADER_TOKEN_STR = "X-Auth-Token";

	@Inject
	private WsgConfig config;
	
	@Override
	public void route(Vertx vertx, Router router) {
		authentication = IocContext.getBean(AbstractAuthentication.class);
		if (authentication != null) {
			router.post("/login").blockingHandler(login(), false);
			router.post("/logout").blockingHandler(logout(), false);
			router.post(config.getPathPrefix() + "/*").blockingHandler(checkToken(), false);
		}
	}

	private Handler<RoutingContext> login() {
		return context -> {
			String bodyJsonString = context.getBodyAsString();
			JSONObject body = JSON.parseObject(bodyJsonString);
			String username = body.getString("username");
			String password = body.getString("password");
			log.debug("username " + username + " login...");
			authentication.login(username, password).setHandler(res -> {
				if (res.succeeded()) {
					end(context, res.result());
				} else {
					fail(context, res.cause());
				}
			});
		};
	}

	private Handler<RoutingContext> logout() {
		return context -> {
			String bodyJsonString = context.getBodyAsString();
			JSONObject body = JSON.parseObject(bodyJsonString);
			String userId = body.getString("userId");
			authentication.logout(userId).setHandler(res -> {
				if (res.succeeded()) {
					end(context, null);
				} else {
					fail(context, res.cause());
				}
			});
		};
	}

	private void end(RoutingContext context, Object object) {
		HttpServerResponse response = context.response();
		response.putHeader(HttpHeaderNames.CONTENT_TYPE,
				HttpHeaderValues.APPLICATION_JSON);
		if (object != null) {
			String resultJson = JSON.toJSONString(object);
			String result = ResultTemplate.success(resultJson);
			response.end(result);
		} else {
			response.end();
		}
	}

	private void fail(RoutingContext context, Throwable e) {
		String result = "";
		if (e instanceof ServiceException) {
			ServiceException re = (ServiceException)e;
			log.error("throw exception: " + re.failureCode() + "::" + re.getMessage());
			result = ResultTemplate.fail(re.failureCode() + "", re.getMessage());
		} else {
			log.error("other error", e);
			result = ResultTemplate.fail("500", e.getMessage());
		}
		
		context.response().end(result);
	}
	
	private Handler<RoutingContext> checkToken() {
		return context -> {
			String token = context.request().getHeader(HEADER_TOKEN_STR);

			if (ObjUtil.isEmpty(token)) {
				ExceptionFactory.throwEx(AuthErrCode.AUTH_TOKEN_NULL);
			}

			authentication.checkToken(token).setHandler(res -> {
				if (res.succeeded()) {
					context.put(GlobalConstant.CURRENT_USER, JSON.toJSONString(res.result()));
					context.next();
				} else {
					fail(context, res.cause());
				}
			});
		};
	}

}
