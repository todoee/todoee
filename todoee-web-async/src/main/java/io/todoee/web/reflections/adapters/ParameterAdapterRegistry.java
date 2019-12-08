package io.todoee.web.reflections.adapters;

import io.todoee.web.reflections.adapters.impl.DefaultParameterAdapter;
import io.vertx.core.MultiMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author James.zhang
 *
 */
public class ParameterAdapterRegistry {

	private final Map<Class<?>, ParameterAdapter<?>> adapters;
	private final ParameterAdapter<Object> defaultParameterAdapter;

	private ParameterAdapterRegistry(ParameterAdapter<Object> defaultParameterAdapter) {
		adapters = new HashMap<>();
		this.defaultParameterAdapter = defaultParameterAdapter;
	}

	public ParameterAdapterRegistry() {
		this(new DefaultParameterAdapter());
	}

	public <T> void registerAdapter(Class<T> parameterClass, ParameterAdapter<T> adapter) {
		adapters.put(parameterClass, adapter);
	}

	@SuppressWarnings("unchecked")
	private <T> ParameterAdapter<T> getAdapter(Class<T> parameterClass) {
		return (ParameterAdapter<T>) adapters.get(parameterClass);
	}

	public ParameterAdapter<Object> getDefaultParameterAdapter() {
		return defaultParameterAdapter;
	}

	@SuppressWarnings("unchecked")
	public <T> T adaptParam(String value, Class<T> parameterClass) {
		ParameterAdapter<T> adapter = getAdapter(parameterClass);
		if (adapter != null) {
			return adapter.adaptParam(value, parameterClass);
		} else if (defaultParameterAdapter != null) {
			return (T) defaultParameterAdapter.adaptParam(value, parameterClass);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T adaptParams(MultiMap values, Class<T> parameterClass) {
		ParameterAdapter<T> adapter = getAdapter(parameterClass);
		if (adapter != null) {
			return adapter.adaptParams(values, parameterClass);
		} else if (defaultParameterAdapter != null) {
			return (T) defaultParameterAdapter.adaptParams(values, parameterClass);
		}
		return null;
	}
}
