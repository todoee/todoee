package io.todoee.httpclient.test.server;

import com.alibaba.fastjson.JSON;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.todoee.httpclient.base.MediaType;
import io.todoee.httpclient.test.rdi.dto.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestVerticle extends AbstractVerticle {

	private HttpServer server;

	private Router router;

	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		router = router(vertx);
		server = server(vertx);
	}

	private Router router(Vertx vertx) {
		return Router.router(vertx);
	}

	private HttpServer server(Vertx vertx) {
		HttpServerOptions options = new HttpServerOptions();
		return vertx.createHttpServer(options);
	}

	@Override
	public void start() throws Exception {
		router.get("/getuser").handler(context -> {
			String id = context.request().getParam("id");
			System.out.println("id: " + id);
			HttpServerResponse response = context.response();
			response.putHeader(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON);
			User user = new User();
			user.setId("123");
			user.setName("zhangsan@163.com");
			user.setNickname("zhangsan");
			response.end(JSON.toJSONString(user));
		});
		
		router.get("/getuser2/:id").handler(context -> {
			String id = context.request().getParam("id");
			System.out.println("id: " + id);
			HttpServerResponse response = context.response();
			response.putHeader(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON);
			response.end();
		});

		server.requestHandler(router).listen(8080, result -> {
			if (result.succeeded()) {
				log.info("Test Server startup on port " + 8080);
			} else {
				log.error("Test Server startup Fail");
			}
		});
	}
}
