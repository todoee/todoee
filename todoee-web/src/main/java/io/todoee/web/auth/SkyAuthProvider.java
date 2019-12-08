package io.todoee.web.auth;

import lombok.extern.slf4j.Slf4j;
import io.todoee.web.auth.param.AuthToken;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class SkyAuthProvider implements SkyAuth {

	private AuthRealm realm;

	private Vertx vertx;

	public SkyAuthProvider(Vertx vertx, AuthRealm realm) {
		this.vertx = vertx;
		this.realm = realm;
	}

	@Override
	public void authenticate(JsonObject authInfo,
			Handler<AsyncResult<User>> resultHandler) {
		vertx.executeBlocking(fut -> {
			SkyUser skyUser;
			try {
				skyUser = login(authInfo);
			} catch (Exception e) {
				log.error("login error", e);
				throw new VertxException(e);
			}
			fut.complete(skyUser);
		}, resultHandler);
	}

//	private SkyUser toSkyUser(AuthenticationInfo info) {
//		SkyUser skyUser = new SkyUser();
//		skyUser.setUname(info.getUsername());
//		skyUser.setUserId(info.getId());
//		skyUser.setActivate(info.getActivate());
//		skyUser.setAvatar(info.getAvatar());
//		return skyUser;
//	}

	private SkyUser login(JsonObject authInfo) {
		AuthToken authToken = new AuthToken();
		authToken.setUsername(authInfo.getString("username"));
		authToken.setPassword(authInfo.getString("password"));
		return realm.doAuth(authToken);
//		return toSkyUser(info);
	}

	public AuthRealm getRealm() {
		return realm;
	}

	public Vertx getVertx() {
		return vertx;
	}
}
