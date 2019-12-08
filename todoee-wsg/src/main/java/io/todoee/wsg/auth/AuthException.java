package io.todoee.wsg.auth;

/**
 * 
 * @author James.zhang
 *
 */
public class AuthException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return null;
	}
}
