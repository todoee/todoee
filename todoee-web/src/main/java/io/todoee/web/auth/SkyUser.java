package io.todoee.web.auth;

import io.todoee.web.constants.AuthConstant;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

/**
 * 
 * @author James.zhang
 *
 */
public class SkyUser extends AbstractUser {

	private String DEFAULT_ROLE_PREFIX = "role:";
	
	private AuthRealm realm;

	private Vertx vertx;
	
	private String rolePrefix = DEFAULT_ROLE_PREFIX;
	
	private JsonObject principal = new JsonObject();

	public String getId() {
		return principal.getString(AuthConstant.ID);
	}

	public void setId(String id) {
		principal.put(AuthConstant.ID, id);
	}

	public String getAccountId() {
		return principal.getString(AuthConstant.ACCOUNTID);
	}

	public void setAccountId(String accountId) {
		principal.put(AuthConstant.ACCOUNTID, accountId);
	}

	public String getNickname() {
		return principal.getString(AuthConstant.NICKNAME);
	}

	public void setNickname(String nickname) {
		principal.put(AuthConstant.NICKNAME, nickname);
	}

	public String getRoles() {
		return principal.getString(AuthConstant.ROLES);
	}

	public void setRoles(String roles) {
		principal.put(AuthConstant.ROLES, roles);
	}

	public String getEmail() {
		return principal.getString(AuthConstant.EMAIL);
	}

	public void setEmail(String email) {
		principal.put(AuthConstant.EMAIL, email);
	}

	public String getMobile() {
		return principal.getString(AuthConstant.MOBILE);
	}

	public void setMobile(String mobile) {
		principal.put(AuthConstant.MOBILE, mobile);
	}
	
	public String getAvatar() {
		return principal.getString(AuthConstant.AVATAR);
	}
	
	public void setAvatar(String avatar) {
		principal.put(AuthConstant.AVATAR, avatar);
	}
	
	public Integer getExp() {
		return principal.getInteger(AuthConstant.EXP);
	}
	
	public void setExp(Integer exp) {
		principal.put(AuthConstant.EXP, exp);
	}
	
	public boolean getActivate() {
		return principal.getBoolean(AuthConstant.ACTIVATE);
	}
	
	public void setActivate(boolean activate) {
		principal.put(AuthConstant.ACTIVATE, activate);
	}
	
	@Override
	public void doIsPermitted(String permissionOrRole, Handler<AsyncResult<Boolean>> resultHandler) {
		if (permissionOrRole.startsWith(rolePrefix)) {
			final String role = permissionOrRole.substring(rolePrefix.length());
			vertx.executeBlocking(fut -> {
				fut.complete(realm.hasRole(role));
			}, resultHandler);
		} else {
			vertx.executeBlocking(fut -> {
				fut.complete(realm.isPermitted(permissionOrRole));
			}, resultHandler);
		}
	}

	@Override
	public User clearCache() {
		this.principal.clear();
		return super.clearCache();
	}
	
	@Override
	public JsonObject principal() {
		return principal;
	}

	@Override
	public void setAuthProvider(AuthProvider authProvider) {
		if (authProvider instanceof SkyAuthProvider) {
			SkyAuthProvider skyAuthProvider = (SkyAuthProvider)authProvider;
			this.realm = skyAuthProvider.getRealm();
			this.vertx = skyAuthProvider.getVertx();
		} else {
			throw new IllegalArgumentException("Not a SkyAuthProviderImpl");
		}
	}

	@Override
	public void writeToBuffer(Buffer buff) {
		super.writeToBuffer(buff);
		this.principal.writeToBuffer(buff);
	}

	@Override
	public int readFromBuffer(int pos, Buffer buffer) {
		pos = super.readFromBuffer(pos, buffer);
		return this.principal.readFromBuffer(pos, buffer);
	}

}