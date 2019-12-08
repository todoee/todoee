package io.todoee.httpclient.test.biz;

import com.google.inject.ImplementedBy;

import io.todoee.httpclient.test.biz.impl.TestBizImpl;
import io.todoee.httpclient.test.rdi.dto.User;
import io.vertx.core.Future;

@ImplementedBy(TestBizImpl.class)
public interface TestBiz {
	
	Future<User> login(String id);
	
	Future<Void> login2(String id);
	
}
