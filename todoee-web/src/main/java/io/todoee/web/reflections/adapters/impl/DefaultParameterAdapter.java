package io.todoee.web.reflections.adapters.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.todoee.base.utils.DateUtils;
import io.todoee.web.reflections.adapters.ParameterAdapter;
import io.vertx.core.MultiMap;

public class DefaultParameterAdapter implements ParameterAdapter<Object> {

	private static final Map<Class<?>, Function<String, Object>> adapters = new HashMap<>();

	static {
		adapters.put(String.class, String::valueOf);
		adapters.put(Long.class, Long::valueOf);
		adapters.put(Integer.class, Integer::valueOf);
		adapters.put(Float.class, Float::valueOf);
		adapters.put(Boolean.class, Boolean::valueOf);
		adapters.put(Date.class, DateUtils.INSTANCE::parseIso8601);

		adapters.put(int.class, Integer::valueOf);
		adapters.put(long.class, Long::valueOf);
		adapters.put(float.class, Float::valueOf);
		adapters.put(boolean.class, Boolean::valueOf);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object adaptParam(String value, Class<?> parameterClass) {
		if (value == null) {
			return null;
		}
		Function<String, Object> adapter = adapters.get(parameterClass);
		if (adapter != null) {
			return adapter.apply(value);
		} else if (parameterClass.isEnum()) {
			return Enum.valueOf((Class<Enum>) parameterClass, value);
		}
		return null;
	}

	@Override
	public Object adaptParams(MultiMap params, Class<?> parameterClass) {
		Object instance;
		try {
			instance = parameterClass.newInstance();

			Field[] fields = parameterClass.getDeclaredFields();
			for (Field field : fields) {
				String requestValue = params.get(field.getName());
				if (requestValue != null) {
					Object value = adaptParam(requestValue, field.getType());
					field.setAccessible(true);
					field.set(instance, value);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return instance;
	}

}
