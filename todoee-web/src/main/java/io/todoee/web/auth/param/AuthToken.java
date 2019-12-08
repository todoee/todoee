package io.todoee.web.auth.param;

import lombok.Data;

@Data
public class AuthToken {
	private String username;
	private String password;
}
