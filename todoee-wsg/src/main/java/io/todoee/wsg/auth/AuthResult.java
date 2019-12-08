package io.todoee.wsg.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Data
public class AuthResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String username;
	
	private String type;

	private String token;
	
	private Map<String, String> ext = new HashMap<>();
}
