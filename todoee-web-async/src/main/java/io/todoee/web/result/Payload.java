package io.todoee.web.result;

import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Data
public class Payload {

	private Integer code;
	private Integer status;
	private String message;
	private String msg;
	private String action;
	private Long total;
	private Object data;

	private Payload() {
		this.code = 0;
		this.status = 0;
		this.message = "success";
		this.msg = "success";
	}
	
	private Payload(Object data) {
		this();
		this.data = data;
	}

	public static Payload getInstance(Object data) {
		Payload payload = new Payload(data);
		return payload;
	}
	
	public static Payload getInstance() {
		Payload payload = new Payload();
		return payload;
	}
}
