package io.todoee.web.routing;

import io.todoee.web.annotations.method.*;
import io.vertx.core.http.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author James.zhang
 *
 */
public final class HttpMethodFactory {

	private static List<Class<? extends Annotation>> annotClasses = Arrays.asList(Delete.class, Get.class, Post.class,
			Put.class, View.class);

	private HttpMethodFactory() {
	}

	public static Map<HttpMethod, String[]> fromAnnotatedMethod(Method method) {
		Map<HttpMethod, String[]> methods = new EnumMap<>(HttpMethod.class);
		for (Annotation annot : method.getDeclaredAnnotations()) {
			putIfHttpMethod(methods, annot);
		}
		return methods;
	}

	public static boolean isRouteMethod(Method method) {
		return annotClasses.stream().anyMatch(method::isAnnotationPresent);
	}

	private static void putIfHttpMethod(Map<HttpMethod, String[]> methods, Annotation annot) {
		Class<? extends Annotation> annotClass = annot.annotationType();
		String[] annoInfo = new String[2];
		if (annotClass.equals(View.class)) {
			View view = (View) annot;
			annoInfo[0] = view.value();
			annoInfo[1] = "false";
			methods.put(HttpMethod.GET, annoInfo);
		} else
		if (annotClass.equals(Delete.class)) {
			Delete delete = (Delete) annot;
			annoInfo[0] = delete.value();
			annoInfo[1] = delete.payload() + "";
			methods.put(HttpMethod.DELETE, annoInfo);
		} else
		if (annotClass.equals(Get.class)) {
			Get get = (Get) annot;
			annoInfo[0] = get.value();
			annoInfo[1] = get.payload() + "";
			methods.put(HttpMethod.GET, annoInfo);
		} else
		if (annotClass.equals(Post.class)) {
			Post post = (Post) annot;
			annoInfo[0] = post.value();
			annoInfo[1] = post.payload() + "";
			methods.put(HttpMethod.POST, annoInfo);
		} else
		if (annotClass.equals(Put.class)) {
			Put put = (Put) annot;
			annoInfo[0] = put.value();
			annoInfo[1] = put.payload() + "";
			methods.put(HttpMethod.PUT, annoInfo);
		}
	}

}
