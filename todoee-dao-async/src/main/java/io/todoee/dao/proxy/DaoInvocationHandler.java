package io.todoee.dao.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.todoee.dao.annotation.Delete;
import io.todoee.dao.annotation.Insert;
import io.todoee.dao.annotation.Select;
import io.todoee.dao.annotation.Update;
import io.todoee.dao.session.SqlSession;
import io.todoee.dao.session.SqlSessionFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
		Future<Object> future = Future.future();
		if (args != null && args.length > 1) {
			throw new RuntimeException("method " + method.getName() + " args count > 1");
		}

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
			if (args[0] instanceof String) {
				clause = (String) args[0];
			} else if (args[0] instanceof JsonObject) {
				JsonObject param = (JsonObject) args[0];
				String key = m.group().replace("${", "").replace("}", "");
				clause = param.getString(key);
			}
			sqlStr = sqlStr.replace(m.group(), clause);
		}

		List<String> list = new ArrayList<>();
		p = Pattern.compile("(\\#\\{.*?\\})");
		m = p.matcher(sqlStr);
		while (m.find()) {
			list.add(m.group());
			if (args[0] instanceof JsonObject) {
				JsonObject param = (JsonObject) args[0];
				Object value = findValue(param, m.group());
				if (value instanceof List) {
					List v = (List) value;
					StringBuffer replace = new StringBuffer();
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
		
		String fullSql = sqlStr;
		JsonArray params = getParams(method, args, future, list);
		Class<?> returnType = getReturnType(method, future);
		
		sessionFactory.openSession().setHandler(h -> {
			if (h.succeeded()) {
				SqlSession session = h.result();
				if (method.isAnnotationPresent(Select.class)) {
					if (JsonObject.class.isAssignableFrom(returnType)) {
						session.get(fullSql, params).setHandler(handler(JsonObject.class, session, future));
					} else if (JsonArray.class.isAssignableFrom(returnType)) {
						session.list(fullSql, params).setHandler(handler(JsonArray.class, session, future));
					} else if (returnType.getName().equals("void")) {
						future.complete();
					} else if (returnType.getName().equals("long") || Long.class.isAssignableFrom(returnType)) {
						session.count(fullSql, params).setHandler(handler(Long.class, session, future));
					} else {
						String error = "select not support type: " + returnType;
						future.fail(error);
					}
				} else {
					if (returnType.getName().equals("void") || returnType.getName().equals("int")
							|| returnType.getName().equals(Integer.class.getName())) {
						session.update(fullSql, params).setHandler(handler(Integer.class, session, future));
					} else {
						String error = "update not support type: " + returnType;
						future.fail(error);
					}
				}
			} else {
				future.fail(h.cause());
			}
		});
		return future;
	}
	
	private <T> Handler<AsyncResult<T>> handler(Class<T> clazz, SqlSession session, Future<Object> future) {
		return o -> {
			if (o.succeeded()) {
				if (!session.transactional()) {
					session.close().setHandler(v -> {
						if (v.succeeded()) {
							log.debug("Closed Session by Connection [" + session.getConnection() + "]");
							future.complete(o.result());
						} else {
							future.fail(v.cause());
						}
					});
				} else {
					future.complete(o.result());
				}
			} else {
				if (!session.transactional()) {
					session.close().setHandler(v -> {
						if (v.succeeded()) {
							log.debug("Closed Session by Connection [" + session.getConnection() + "]");
							future.fail(o.cause());
						} else {
							future.fail(v.cause());
						}
					});
				} else {
					future.fail(o.cause());
				}
			}
		};
	}
	
	private JsonArray getParams(Method method, Object[] args, Future<Object> future, List<String> list) {
		JsonArray params = new JsonArray();
		if (args == null || args.length == 0) {
			return params;
		}
		if (args[0] instanceof String || isPrimitive(args[0])) {
			params.add(args[0]);
		} else if (args[0] instanceof JsonObject) {
			params = values((JsonObject) args[0], list);
		} else {
			String error = "method " + method.getName() + " args class not support";
			future.fail(error);
			throw new RuntimeException(error);
		}
		return params;
	}

	private Class<?> getReturnType(Method method, Future<Object> future) {
		Type t = method.getGenericReturnType();
		Class<?> returnType = null;
	    if (t instanceof ParameterizedType) {
	    	Type type = ((ParameterizedType) t).getActualTypeArguments()[0];
	    	returnType = (Class<?>)type;
	    } else {
	    	String error = "method: " + method.getDeclaringClass().getName() + "." + method.getName() + " return type not valid";
	    	future.fail(error);
	    	throw new RuntimeException(error);
	    }
		return returnType;
	}

	private static boolean isPrimitive(Object obj) {
		try {
			return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JsonArray values(JsonObject jsonObj, List<String> list) {
		JsonArray valueList = new JsonArray();
		for (String name : list) {
			Object value = findValue(jsonObj, name);
			if (value instanceof List) {
				List v = (List) value;
				v.forEach(item -> {
					valueList.add(item);
				});
			} else {
				valueList.add(value);
			}
		}
		return valueList;
	}

	private Object findValue(JsonObject jsonObj, String name) {
		name = name.replace("#{", "").replace("}", "");
		Object value = jsonObj.getValue(name);
		return value;
	}
}
