package io.todoee.web.reflections.injectors.typed.impl;

import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

public class SessionParamInjector implements ParamInjector<Session> {

  @Override
  public Session resolve(RoutingContext context) {
    return context.session();
  }

}
