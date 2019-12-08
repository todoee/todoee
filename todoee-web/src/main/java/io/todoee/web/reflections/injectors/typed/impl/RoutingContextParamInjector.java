package io.todoee.web.reflections.injectors.typed.impl;

import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.vertx.ext.web.RoutingContext;

public class RoutingContextParamInjector implements ParamInjector<RoutingContext> {

  @Override
  public RoutingContext resolve(RoutingContext context) {
    return context;
  }

}
