package io.todoee.dao.test.biz;

import com.google.inject.ImplementedBy;

import io.todoee.dao.test.biz.impl.AdminBizImpl;
import io.vertx.core.Future;

@ImplementedBy(AdminBizImpl.class)
public interface AdminBiz {
	
	Future<Void> login();
	
}
