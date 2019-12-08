package io.todoee.dao.transaction;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import io.todoee.dao.session.SqlSession;
import io.todoee.dao.session.SqlSessionFactory;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 */
@Slf4j
public class TransactionAop implements MethodInterceptor {

	@Inject
	private SqlSessionFactory sessionFactory;

	@Override
	public Object invoke(MethodInvocation invocation) {
		log.debug("JDBC Transaction Begin");
		Future<Object> future = Future.future();
		sessionFactory.openSession(false).setHandler(s -> {
			if (s.succeeded()) {
				SqlSession session = s.result();
					Object obj = null;
					try {
						obj = invocation.proceed();
					} catch (Throwable e) {
						throw new RuntimeException(e);
					}
					Future<?> f = (Future<?>)obj;
					Object result = obj;
					f.setHandler(a -> {
						if (a.succeeded()) {
							Future<Void> commit = Future.future(h -> {
								session.commit().setHandler(h);
							});
							commit.compose(v -> {
								log.debug("JDBC Transaction commit");
								return sessionFactory.closeSession();
							}).setHandler(res -> {
								if (res.succeeded()) {
									log.debug("Closed JDBC Connection: " + session.getConnection());
									future.complete(result);
								} else {
									future.fail(res.cause());
								}
							});
						} else {
							Future<Void> rollback = Future.future(h -> {
								session.rollback().setHandler(h);
							});
							rollback.compose(v -> {
								log.debug("JDBC Transaction rollback");
								return sessionFactory.closeSession();
							}).setHandler(res -> {
								if (res.succeeded()) {
									log.debug("Closed connection: " + session.getConnection());
									future.fail(a.cause());
								} else {
									future.fail(res.cause());
								}
							});
						}
					});
			} else {
				future.fail(s.cause());
			}
		});
		return future;
	}
}
