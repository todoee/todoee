package io.todoee.httpclient.proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Data
@Slf4j
@Singleton
public class WebClientFactory {

	private WebClient client;
	
	@Inject
	public WebClientFactory(Vertx vertx) {
		init(vertx);
	}

	private void init(Vertx vertx) {
		log.info("Init Web Client Factory");
		WebClientOptions options = new WebClientOptions();
		this.client = WebClient.create(vertx, options);
	}
	
	public WebClient client() {
		return this.client;
	}
}
