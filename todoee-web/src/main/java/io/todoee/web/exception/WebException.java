package io.todoee.web.exception;

import io.todoee.core.exception.ServiceException;

/**
 * 
 * @author James.zhang
 *
 */
public class WebException extends ServiceException {

	private static final long serialVersionUID = 1L;
	
	public WebException(String message) {
		this(600, message);
	}
	
	public WebException(Integer errCode, String message) {
		super(errCode, message);
	}
}
