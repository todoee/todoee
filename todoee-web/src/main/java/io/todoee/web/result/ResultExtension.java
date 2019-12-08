package io.todoee.web.result;

import java.util.Map;

import lombok.ToString;

/**
 * 
 * @author James.zhang
 *
 */
@ToString
public class ResultExtension {
	
	private static ThreadLocal<Map<String, Object>> extension = new ThreadLocal<>();
	
	public static void setExtension(Map<String, Object> map) {
		extension.set(map);
	}
	
	public static Map<String, Object> getExtension() {
		return extension.get();
	}
	
	public static void remove() {
		extension.remove();
	}
	
}
