package io.todoee.web.reflections.injectors.typed.impl;

import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class HttpResponseParamInjector implements ParamInjector<HttpServerResponse> {

  @Override
  public HttpServerResponse resolve(RoutingContext context) {
    return context.response();
  }

}
