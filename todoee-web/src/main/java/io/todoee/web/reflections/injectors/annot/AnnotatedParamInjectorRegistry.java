package io.todoee.web.reflections.injectors.annot;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import io.todoee.web.annotations.params.*;
import io.todoee.web.reflections.adapters.ParameterAdapterRegistry;
import io.todoee.web.reflections.injectors.annot.impl.*;

public class AnnotatedParamInjectorRegistry {

	private final Map<Class<?>, AnnotatedParamInjector<?>> map;

	public AnnotatedParamInjectorRegistry(ParameterAdapterRegistry adapters) {
		map = new HashMap<>();
		registerInjector(RequestBody.class, new RequestBodyParamInjector());
		
		registerInjector(CookieParam.class, new CookieParamInjector());
		
		registerInjector(HeaderParam.class, new HeaderParamInjector(adapters));
		
		registerInjector(PathParam.class, new PathParamInjector(adapters));
		
		registerInjector(QueryParam.class, new RequestParamInjector(adapters));
		registerInjector(QueryParams.class, new RequestParamsInjector(adapters));
		
		registerInjector(FormParam.class, new FormParamInjector(adapters));
		registerInjector(FormParams.class, new FormParamsInjector(adapters));
		
		registerInjector(LocalMapValue.class, new LocalMapValueParamInjector());
		registerInjector(VertxLocalMap.class, new LocalMapParamInjector());
		registerInjector(ContextData.class, new ContextDataParamInjector());
	}

	private <T extends Annotation> void registerInjector(Class<? extends T> clazz, AnnotatedParamInjector<T> injector) {
		map.put(clazz, injector);
	}

	@SuppressWarnings("unchecked")
	public <T extends Annotation> AnnotatedParamInjector<T> getInjector(Class<? extends T> clazz) {
		return (AnnotatedParamInjector<T>) map.get(clazz);
	}

}
