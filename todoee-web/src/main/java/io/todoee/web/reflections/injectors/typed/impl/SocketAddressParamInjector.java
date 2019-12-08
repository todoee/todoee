package io.todoee.web.reflections.injectors.typed.impl;

import io.todoee.web.reflections.injectors.typed.ParamInjector;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;

public class SocketAddressParamInjector implements ParamInjector<SocketAddress> {

  @Override
  public SocketAddress resolve(RoutingContext context) {
    return context.request().remoteAddress();
  }

}
