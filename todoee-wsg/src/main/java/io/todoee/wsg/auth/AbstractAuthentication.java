package io.todoee.wsg.auth;

import io.todoee.core.context.IocContext;
import io.vertx.core.Future;

/**
 * 
 * @author James.zhang
 *
 */
public abstract class AbstractAuthentication {
	
	public Future<AuthResult> login(String username, String password) {
		Future<AuthResult> future = Future.future();
		AuthResult authResult = null;
		try {
			authResult = loginAction(username, password);
		} catch(Exception e) {
//			ExceptionFactory.throwEx(AuthErrCode.LOGIN_ERROR);
			future.fail(e);
			return future;
		}
		TokenOper tokenOper = IocContext.getBean(TokenOper.class);
		return tokenOper.save(authResult);
	}
	
	public Future<Void> logout(String userId) {
		Future<Void> future = Future.future();
		TokenOper tokenOper = IocContext.getBean(TokenOper.class);
		tokenOper.clear(userId).setHandler(res -> {
			if (res.succeeded()) {
				try {
					logoutAction(userId);
					future.complete();
				} catch (Exception e) {
					future.fail(e);
				}
			} else {
				future.fail(res.cause());
			}
		});
		return future;
	}
	
	public Future<AuthResult> checkToken(String token) {
		Future<AuthResult> future = Future.future();
		TokenOper tokenOper = IocContext.getBean(TokenOper.class);
		try {
			return tokenOper.check(token);
		} catch(Exception e) {
			future.fail(e);
		}
		return future;
	}
	
	protected abstract AuthResult loginAction(String username, String password);
	
	protected abstract void logoutAction(String userId);
	
}
