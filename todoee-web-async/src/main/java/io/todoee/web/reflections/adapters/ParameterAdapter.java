package io.todoee.web.reflections.adapters;

import io.vertx.core.MultiMap;

/**
 * 
 * @author James.zhang
 *
 */
public interface ParameterAdapter<T> {

	T adaptParam(String value, Class<? extends T> parameterClass);

	T adaptParams(MultiMap map, Class<? extends T> parameterClass);

}
