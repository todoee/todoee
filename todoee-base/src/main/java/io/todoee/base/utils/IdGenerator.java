package io.todoee.base.utils;

import java.util.UUID;

/**
 * 
 * @author James.zhang
 *
 */
public class IdGenerator {
	
	public static String get() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}
	
}
