package io.todoee.dao.test;

import org.junit.Before;
import org.junit.Test;

import io.todoee.core.context.IocContext;
import io.todoee.dao.test.biz.AdminBiz;
import io.vertx.core.Vertx;

public class DaoTest {
	
	@Before
    public void setUp() {
        IocContext.build(Vertx.vertx());
    }
	
	@Test
	public void reg() {
//		UserDao userDao = IocContext.getBean(UserDao.class);
		AdminBiz userBiz = IocContext.getBean(AdminBiz.class);
		userBiz.login().setHandler(h -> {
			if (h.succeeded()) {
				System.out.println("biz success");
			} else {
				h.cause().printStackTrace();
			}
		});
//		userDao.get("029bce56071349ce90ce7efb2cad368c4").setHandler(res -> {
//			if (res.succeeded()) {
//				System.out.println(res.result());
//			} else {
//				res.cause().printStackTrace();
//			}
//		});
//		userDao.list().setHandler(res -> {
//			if (res.succeeded()) {
//				System.out.println(res.result());
//			} else {
//				res.cause().printStackTrace();
//			}
//		});
		
//		userDao.count().setHandler(res -> {
//			if (res.succeeded()) {
//				System.out.println(res.result());
//			} else {
//				res.cause().printStackTrace();
//			}
//		});
		
//		JsonObject jo = new JsonObject();
//		jo.put("id", "029bce56071349ce90ce7efb2cad368c4");
//		jo.put("nickname", "duanfei");
//		userDao.update(jo).setHandler(res -> {
//			if (res.succeeded()) {
//				System.out.println(res.result());
//			} else {
//				res.cause().printStackTrace();
//			}
//		});
		
		waiting(1000000);
	}
	
	private void waiting(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
