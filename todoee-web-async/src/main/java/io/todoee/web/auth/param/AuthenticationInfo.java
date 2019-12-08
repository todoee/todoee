package io.todoee.web.auth.param;

import lombok.Data;

@Data
public class AuthenticationInfo {
	private String id;
	private String username;
	private String avatar;
	private String email;
	private String mobile;
	private String rank;
	private Boolean activate;
}
