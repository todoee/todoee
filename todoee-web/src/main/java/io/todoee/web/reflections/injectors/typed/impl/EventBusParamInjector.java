package io.todoee.web.reflections.injectors.typed.impl;

import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class EventBusParamInjector implements ParamInjector<EventBus> {

  @Override
  public EventBus resolve(RoutingContext context) {
    return context.vertx().eventBus();
  }

}
