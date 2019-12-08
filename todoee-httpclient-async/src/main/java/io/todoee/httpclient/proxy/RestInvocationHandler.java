package io.todoee.httpclient.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.todoee.httpclient.annotation.Consume;
import io.todoee.httpclient.annotation.DELETE;
import io.todoee.httpclient.annotation.FormParam;
import io.todoee.httpclient.annotation.GET;
import io.todoee.httpclient.annotation.HeaderParam;
import io.todoee.httpclient.annotation.Host;
import io.todoee.httpclient.annotation.JsonParam;
import io.todoee.httpclient.annotation.POST;
import io.todoee.httpclient.annotation.PUT;
import io.todoee.httpclient.annotation.Path;
import io.todoee.httpclient.annotation.PathParam;
import io.todoee.httpclient.annotation.Port;
import io.todoee.httpclient.annotation.Produce;
import io.todoee.httpclient.annotation.QueryParam;
import io.todoee.httpclient.base.MediaType;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 * @param <T>
 */
@Slf4j
@Singleton
public class RestInvocationHandler<T> implements InvocationHandler {

	@Inject
	private WebClientFactory webClientFactory;

	private static final String DEFAULT_HOST = "0.0.0.0";
	private static final Integer DEFAULT_PORT = 8080;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Future<Object> future = Future.future();
		Class<?> classType = method.getDeclaringClass();
		String host = DEFAULT_HOST;
		Integer port = DEFAULT_PORT;
		String classPath = "";
		if (classType.isAnnotationPresent(Host.class)) {
			host = classType.getDeclaredAnnotation(Host.class).value();
		}
		if (classType.isAnnotationPresent(Port.class)) {
			port = classType.getDeclaredAnnotation(Port.class).value();
		}
		if (classType.isAnnotationPresent(Path.class)) {
			classPath = classType.getDeclaredAnnotation(Path.class).value();
		}

		Class<? extends Annotation> action;

		String methodPath = "";
		if (method.isAnnotationPresent(Path.class)) {
			methodPath = method.getDeclaredAnnotation(Path.class).value();
		}

		String path = classPath + methodPath;
		WebClient client = webClientFactory.client();
		HttpRequest<Buffer> request;
		if (method.isAnnotationPresent(PUT.class)) {
			action = PUT.class;
			request = client.put(port, host, path);
		} else if (method.isAnnotationPresent(POST.class)) {
			action = POST.class;
			request = client.post(port, host, path);
		} else if (method.isAnnotationPresent(DELETE.class)) {
			action = DELETE.class;
			request = client.delete(port, host, path);
		} else if (method.isAnnotationPresent(GET.class)) {
			action = GET.class;
			request = client.get(port, host, path);
		} else {
			throw new RuntimeException("method " + method.getName() + " not found @GET, @POST, @DELETE, @PUT");
		}

		Parameter[] parameters = method.getParameters();
		MultiMap form = MultiMap.caseInsensitiveMultiMap();
		Object json = null;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].isAnnotationPresent(HeaderParam.class)) {
				request.putHeader(parameters[i].getDeclaredAnnotation(HeaderParam.class).value(), (String) args[i]);
			} else if (parameters[i].isAnnotationPresent(QueryParam.class)) {
				request.addQueryParam(parameters[i].getDeclaredAnnotation(QueryParam.class).value(), (String) args[i]);
			} else if (parameters[i].isAnnotationPresent(PathParam.class)) {
				request.addQueryParam(parameters[i].getDeclaredAnnotation(PathParam.class).value(), (String) args[i]);
			} else if (parameters[i].isAnnotationPresent(FormParam.class)) {
				form.set(parameters[i].getDeclaredAnnotation(FormParam.class).value(), (String) args[i]);
			} else if (parameters[i].isAnnotationPresent(JsonParam.class)) {
				json = args[i];
			} else {
				throw new RuntimeException("method parameter " + parameters[i].getName() + " not found any Annotation");
			}
		}

		String consume = MediaType.APPLICATION_JSON;
		String produce = MediaType.APPLICATION_JSON;
		if (classType.isAnnotationPresent(Consume.class)) {
			consume = classType.getDeclaredAnnotation(Consume.class).value();
		}
		if (method.isAnnotationPresent(Consume.class)) {
			consume = method.getDeclaredAnnotation(Consume.class).value();
		}

		if (classType.isAnnotationPresent(Produce.class)) {
			produce = classType.getDeclaredAnnotation(Produce.class).value();
		}
		if (method.isAnnotationPresent(Produce.class)) {
			produce = method.getDeclaredAnnotation(Produce.class).value();
		}

		if (! produce.equals(MediaType.APPLICATION_JSON)) {
			throw new RuntimeException("method " + method.getName() + " @Produce not support MediaType: " + consume);
		}
		
		Class<T> returnType = getReturnType(method, future);
		HttpRequest<T> newRequest = request.expect(ResponsePredicate.SC_SUCCESS).expect(ResponsePredicate.JSON)
				.as(BodyCodec.json(returnType));

		if (consume.equals(MediaType.APPLICATION_JSON)) {
			if (action.equals(POST.class) || action.equals(PUT.class)) {
				newRequest.sendJson(json, handler(returnType, future));
			} else {
				newRequest.send(handler(returnType, future));
			}

		} else if (consume.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
			if (action.equals(POST.class) || action.equals(PUT.class)) {
				newRequest.sendForm(form, handler(returnType, future));
			} else {
				newRequest.send(handler(returnType, future));
			}
		} else {
			throw new RuntimeException("method " + method.getName() + " @Consume not support MediaType: " + consume);
		}
		return future;
	}

	private Handler<AsyncResult<HttpResponse<T>>> handler(Class<T> clazz, Future<Object> future) {
		return o -> {
			if (o.succeeded()) {
				log.debug("Result Success Return: " + o.result().body());
				future.complete(o.result().body());
			} else {
				future.fail(o.cause());
			}
		};
	}

	@SuppressWarnings("unchecked")
	private Class<T> getReturnType(Method method, Future<Object> future) {
		Type t = method.getGenericReturnType();
		Class<T> returnType = null;
		if (t instanceof ParameterizedType) {
			Type type = ((ParameterizedType) t).getActualTypeArguments()[0];
			returnType = (Class<T>) type;
		} else {
			String error = "method: " + method.getDeclaringClass().getName() + "." + method.getName()
					+ " return type not valid";
			future.fail(error);
			throw new RuntimeException(error);
		}
		return returnType;
	}
}
