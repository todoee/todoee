package io.todoee.dao.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.todoee.dao.annotation.Delete;
import io.todoee.dao.annotation.Insert;
import io.todoee.dao.annotation.Select;
import io.todoee.dao.annotation.Update;
import io.todoee.dao.session.SqlSession;
import io.todoee.dao.session.SqlSessionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
@Singleton
public class DaoInvocationHandler implements InvocationHandler {

	@Inject
	private SqlSessionFactory sessionFactory;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String sqlStr;
		if (method.isAnnotationPresent(Select.class)) {
			sqlStr = method.getDeclaredAnnotation(Select.class).value();
		} else if (method.isAnnotationPresent(Insert.class)) {
			sqlStr = method.getDeclaredAnnotation(Insert.class).value();
		} else if (method.isAnnotationPresent(Update.class)) {
			sqlStr = method.getDeclaredAnnotation(Update.class).value();
		} else if (method.isAnnotationPresent(Delete.class)) {
			sqlStr = method.getDeclaredAnnotation(Delete.class).value();
		} else {
			throw new RuntimeException("method " + method.getName() + " annotation not found");
		}

		Pattern p = Pattern.compile("(\\$\\{.*?\\})");
		Matcher m = p.matcher(sqlStr);
		while (m.find()) {
			String clause = "";
			if (args != null && args.length == 1) {
				if (args[0] instanceof String) {
					clause = (String) args[0];
				} else if (args[0] instanceof JSONObject) {
					JSONObject param = (JSONObject) args[0];
					String key = m.group().replace("${", "").replace("}", "");
					clause = param.getString(key);
				}
				sqlStr = sqlStr.replace(m.group(), clause);
			}
		}
		
		List<String> list = new ArrayList<>();
		p = Pattern.compile("(\\#\\{.*?\\})");
		m = p.matcher(sqlStr);
		while (m.find()) {
			if (args != null && args.length == 1) {
				list.add(m.group());
				if (args[0] instanceof JSONObject) {
					JSONObject param = (JSONObject) args[0];
					Object value = findValue(param, m.group());
					if (value instanceof List) {
						List v = (List)value;
						StringBuffer replace=new StringBuffer();
						for (int i = 0; i < v.size(); i++) {
							replace = replace.append("?,");
						}
						sqlStr = sqlStr.replace(m.group(), replace.deleteCharAt(replace.length() - 1));
					} else {
						sqlStr = sqlStr.replace(m.group(), "?");
					}
				} else {
					sqlStr = sqlStr.replace(m.group(), "?");
				}
			}
		}

		if (args != null && args.length > 1) {
			throw new RuntimeException("method " + method.getName() + " args count > 1");
		}
		Object[] params = new Object[0];

		if (args != null && args.length == 1) {
			if (args[0] instanceof String || isPrimitive(args[0])) {
				params = new Object[1];
				params[0] = args[0];
			} else if (args[0] instanceof JSONObject) {
				params = values((JSONObject) args[0], list);
			} else {
				throw new RuntimeException("method " + method.getName() + " args class not support");
			}
		}
		
		SqlSession session = sessionFactory.openSession();

		Class<?> returnType = method.getReturnType();
		try {
			if (method.isAnnotationPresent(Select.class)) {
				if (JSONObject.class.isAssignableFrom(returnType)) {
					return session.get(sqlStr, params);
				} else if (JSONArray.class.isAssignableFrom(returnType)) {
					return session.list(sqlStr, params);
				} else if (returnType.getName().equals("void")) {
					return null;
				} else if (returnType.getName().equals("long") || Long.class.isAssignableFrom(returnType)) {
					return session.count(sqlStr, params);
				} else {
					throw new RuntimeException("select not support type: " + returnType);
				}
			} else {
				if (returnType.getName().equals("void") 
						|| returnType.getName().equals("int")
						|| returnType.getName().equals(Integer.class.getName())) {
					return session.update(sqlStr, params);
				} else {
					throw new RuntimeException("update not support type: " + returnType);
				}
				
			}
		} catch (Exception e) {
			throw new RuntimeException("dao invoke error", e);
		} finally {
			if (session.getConnection().getAutoCommit()) {
				sessionFactory.closeSession();
			} else {
				log.debug("Connection using transaction");
			}
		}
	}

	private static boolean isPrimitive(Object obj) {
		try {
			return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object[] values(JSONObject jsonObj, List<String> list) {
		List<Object> valueList = new ArrayList<>();
		for (String name : list) {
			Object value = findValue(jsonObj, name);
			if (value instanceof List) {
				List v = (List)value;
				v.forEach(item -> {
					valueList.add(item);
				});
			} else {
				valueList.add(value);
			}
		}
		return valueList.toArray();
	}

	private Object findValue(JSONObject jsonObj, String name) {
		name = name.replace("#{", "").replace("}", "");
		Object value = jsonObj.get(name);
		return value;
	}
}
