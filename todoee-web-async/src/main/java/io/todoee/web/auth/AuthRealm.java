package io.todoee.web.auth;

import io.todoee.web.auth.param.AuthToken;
import io.todoee.web.auth.param.AuthorizationInfo;

public interface AuthRealm {
	
	SkyUser doAuth(AuthToken token);
	
	AuthorizationInfo doGetAuthorizationInfo(String username, String pwd);
	
	boolean hasRole(String roleIdentifier);
	
	boolean isPermitted(String permission);
}
