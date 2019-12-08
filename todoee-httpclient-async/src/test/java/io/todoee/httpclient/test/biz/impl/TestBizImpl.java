package io.todoee.httpclient.test.biz.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.todoee.httpclient.test.biz.TestBiz;
import io.todoee.httpclient.test.rdi.TestRdi;
import io.todoee.httpclient.test.rdi.dto.User;
import io.vertx.core.Future;

@Singleton
public class TestBizImpl implements TestBiz {
	
	@Inject
	private TestRdi rdi;
	
	@Override
	public Future<User> login(String id) {
		return rdi.get(id);
	}

	@Override
	public Future<Void> login2(String id) {
		return rdi.get2(id);
	}
	
}
