package io.todoee.dao.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Data
public class DaoMethodProxyHolder {
	private static Map<String, DaoMethodProxy> proxyMap = new HashMap<>();
	
	public static void put(DaoMethodProxy proxy) {
		proxyMap.put(proxy.getId(), proxy);
	}
	
	public static DaoMethodProxy get(Method method) {
		return proxyMap.get(method.getDeclaringClass().getName() + "." + method.getName());
	}
	
	public static boolean containsKey(String id) {
		return proxyMap.containsKey(id);
	}
}
