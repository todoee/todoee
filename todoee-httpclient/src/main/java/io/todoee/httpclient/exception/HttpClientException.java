package io.todoee.httpclient.exception;

/**
 * 
 * @author James.zhang
 *
 */
public class HttpClientException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private int code;
	private String message;
	
	public HttpClientException(int errCode, String message) {
		this.code = errCode;
		this.message = message;
	}
	
	public void throwException() {
		throw this;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return null;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
