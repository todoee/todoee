package io.todoee.faces.exception;

/**
 * 
 * @author James.zhang
 *
 */
public class BizException extends Throwable {

	private static final long serialVersionUID = 1L;

	private String message;
	
	private String errCode;
	
	public BizException(String message) {
		this.message = message;
	}
	
	public BizException(String errCode, String message) {
		this.errCode = errCode;
		this.message = message;
	}

	@Override
	public Throwable fillInStackTrace() {
		return null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrCode() {
		return errCode;
	}
	
}
