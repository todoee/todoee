package io.todoee.web.reflections.injectors.typed.impl;

import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

public class HttpRequestParamInjector implements ParamInjector<HttpServerRequest> {

  @Override
  public HttpServerRequest resolve(RoutingContext context) {
    return context.request();
  }

}
