package io.todoee.core.util;

import io.todoee.core.boot.BootConfig;
import io.todoee.core.context.IocContext;

/**
 * 
 * @author James.zhang
 *
 */
public class EventbusAddress {
	public static String get(String clazz) {
		BootConfig config = IocContext.getBean(BootConfig.class);
		return config.getServiceGroup() + "_" + clazz;
	}
}
