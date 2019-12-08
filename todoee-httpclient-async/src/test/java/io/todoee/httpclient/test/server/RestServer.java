package io.todoee.httpclient.test.server;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @author James.zhang
 */
@Slf4j
public class RestServer {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(TestVerticle.class, new DeploymentOptions().setInstances(1).setWorker(false), res -> {
			if (res.succeeded()) {
				log.info(RestServer.class.getName() + " deploy success, deployment id: " + res.result());
			} else {
				res.cause().printStackTrace();
			}
		});
	}
}
