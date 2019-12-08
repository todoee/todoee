package io.todoee.wsg.constants;

/**
 * 
 * @author James.zhang
 *
 */
public class ResultTemplate {
	private static final String SUCCESS_RESULT = "{\"code\":\"0\", \"message\":\"call service success\", \"data\":%s}";
	private static final String FAIL_RESULT = "{\"code\":\"%s\", \"message\":\"%s\"}";

	public static String fail(String code, String message) {
		return String.format(FAIL_RESULT, code, message);
	}

	public static String success(String data) {
		return String.format(SUCCESS_RESULT, data);
	}
}
