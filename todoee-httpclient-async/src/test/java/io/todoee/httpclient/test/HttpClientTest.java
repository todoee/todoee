package io.todoee.httpclient.test;

import org.junit.Before;
import org.junit.Test;

import io.todoee.core.context.IocContext;
import io.todoee.httpclient.test.biz.TestBiz;
import io.vertx.core.Vertx;

public class HttpClientTest {
	
	@Before
    public void setUp() {
        IocContext.build(Vertx.vertx());
    }
	
	@Test
	public void reg() {
		TestBiz userBiz = IocContext.getBean(TestBiz.class);
		String id = "123";
//		userBiz.login(id).setHandler(h -> {
//			if (h.succeeded()) {
//				System.out.println("biz success, result: " + h.result());
//			} else {
//				h.cause().printStackTrace();
//			}
//		});
		
		userBiz.login2(id).setHandler(h -> {
			if (h.succeeded()) {
				System.out.println("biz success, result: " + h.result());
			} else {
				h.cause().printStackTrace();
			}
		});
		
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
