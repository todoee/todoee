package io.todoee.dao.test.biz.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.todoee.dao.annotation.Transactional;
import io.todoee.dao.test.biz.AdminBiz;
import io.todoee.dao.test.dao.UserDao;
import io.todoee.dao.test.dao.UserDao2;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class AdminBizImpl implements AdminBiz {
	
	@Inject
	private UserDao userDao;
	
	@Inject
	private UserDao2 userDao2;
	
	@Transactional
	@Override
	public Future<Void> login() {
		Future<Void> future = Future.future();
		Future<JsonObject> startF = Future.future(f -> 
			userDao.get("029bce56071349ce90ce7efb2cad368c4").setHandler(f)
		);
		startF.compose(v -> {
			return userDao2.count();
		}).setHandler(res -> {
			if (res.succeeded()) {
				log.debug("result: " + res.result());
				future.complete();
			} else {
				future.fail(res.cause());
			}
		});
		return future;
	}
	
}
