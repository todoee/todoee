package io.todoee.faces.verticle;

import io.todoee.core.context.IocContext;
import io.todoee.core.verticle.ExternalAbstractVerticle;
import io.todoee.faces.server.EmbeddedServer;
import io.vertx.core.Future;

/**
 * 
 * @author James.zhang
 *
 */
public class FacesVerticle extends ExternalAbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) {
		EmbeddedServer server = IocContext.getBean(EmbeddedServer.class);
		server.start(startFuture);
	}
}
