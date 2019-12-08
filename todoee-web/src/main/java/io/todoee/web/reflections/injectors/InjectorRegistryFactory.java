package io.todoee.web.reflections.injectors;

import io.todoee.web.reflections.adapters.ParameterAdapterRegistry;
import io.todoee.web.reflections.injectors.annot.AnnotatedParamInjectorRegistry;
import io.todoee.web.reflections.injectors.typed.TypedParamInjectorRegistry;

public class InjectorRegistryFactory {
	public static TypedParamInjectorRegistry typeInjectors;
	public static AnnotatedParamInjectorRegistry annotInjectors;
	
	static {
		createTypeInjectors();
		createAnnotInjectors();
	}
	
	private static void createTypeInjectors() {
		typeInjectors = new TypedParamInjectorRegistry();
	}
	
	private static void createAnnotInjectors() {
		ParameterAdapterRegistry registry = new ParameterAdapterRegistry();
		annotInjectors = new AnnotatedParamInjectorRegistry(registry);
	}
	
}
