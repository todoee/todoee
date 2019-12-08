package io.todoee.wsg.auth;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.alibaba.fastjson.JSON;

import io.todoee.core.exception.ServiceException;
import io.todoee.wsg.config.WsgConfig;
import io.vertx.core.Future;

/**
 * @author James.zhang
 * 
 */
@Singleton
public class TokenOper {

	@Inject
	private WsgConfig config;

	@Inject
	private SharedDataHolder holder;

	public Future<AuthResult> save(AuthResult authResult) {
		Future<AuthResult> future = Future.future();
		String authResultJsonStr = JSON.toJSONString(authResult);
		setToken(authResult.getToken(), authResultJsonStr).compose(v -> setUserKey(authResult)).setHandler(future);
		return future;
	}

	private Future<String> setToken(String token, String authResultJsonStr) {
		Future<String> future = Future.future();
		holder.set(token, authResultJsonStr, config.getTokenExpire()).setHandler(res -> {
			if (res.succeeded()) {
				future.complete(authResultJsonStr);
			} else {
				future.fail(res.cause());
			}
		});
		return future;
	}

	private Future<AuthResult> setUserKey(AuthResult authResult) {
		Future<AuthResult> future = Future.future();
		String userKey = getKey(authResult.getUserId(), authResult.getType());
		holder.set(userKey, authResult.getToken(), config.getTokenExpire()).setHandler(res -> {
			if (res.succeeded()) {
				future.complete(authResult);
			} else {
				future.fail(res.cause());
			}
		});
		return future;
	}

	public Future<AuthResult> check(String token) {
		Future<AuthResult> future = Future.future();
		holder.get(token).compose(authResultJsonStr -> {
			if (authResultJsonStr == null) {
				ServiceException se = new ServiceException(1001, "令牌不合法");
				throw se;
			}
			return setToken(token, authResultJsonStr);
		}).compose(authResultJsonStr -> {
			AuthResult authResult = JSON.parseObject(authResultJsonStr, AuthResult.class);
			return setUserKey(authResult);
		}).setHandler(future);
		return future;
	}

	public Future<String> clear(String userId) {
		Future<String> future = Future.future();
		String userKey = getKey(userId, "");
		holder.get(userKey).compose(token -> holder.del(token)).compose(v -> holder.del(userKey)).setHandler(future);
		return future;
	}

	private String getKey(String userId, String type) {
		String key = (type == null ? "" : type) + "_" + userId;
		return key;
	}
}
