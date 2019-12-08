package io.todoee.dao.session;

import javax.inject.Inject;

import io.todoee.dao.transaction.JdbcTransaction;
import io.todoee.dao.transaction.Transaction;
import io.todoee.dao.transaction.TransactionIsolationLevel;
import io.vertx.core.Future;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author James.zhang
 */
@Slf4j
public class DefaultSqlSessionFactory implements SqlSessionFactory {

	@Inject
	private SQLClient client;

	private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<>();

	public Future<SqlSession> openSession() {
		return openSession(true);
	}

	public Future<SqlSession> openSession(boolean autoCommit) {
		Future<SqlSession> future = Future.future();
		SqlSession session = (SqlSession) threadLocal.get();
		if (session == null) {
			if (autoCommit) {
				openNewSession().setHandler(future);
			} else {
				openTransactionalSession(TransactionIsolationLevel.READ_COMMITTED, autoCommit).setHandler(s -> {
					if (s.succeeded()) {
						SqlSession result = s.result();
						threadLocal.set(result);
						future.complete(result);
					} else {
						future.fail(s.cause());
					}
				});
			}
		} else {
			future.complete(session);
		}
		return future;
	}

	@Override
	public Future<Void> closeSession() {
		Future<Void> future = Future.future();
		SqlSession session = (SqlSession) threadLocal.get();
		if (session != null) {
			threadLocal.remove();
			session.close().setHandler(future);
		} else {
			future.fail("Current Session is Null");
		}
		return future;
	}

	private Future<SqlSession> openNewSession() {
		Future<SqlSession> future = Future.future();
		client.getConnection(c -> {
			if (c.succeeded()) {
				SQLConnection conn = c.result();
				log.debug("create new session by connection: " + conn);
				future.complete(new DefaultSqlSession(conn));
			} else {
				future.fail(c.cause());
			}
		});
		return future;
	}

	private Future<SqlSession> openTransactionalSession(TransactionIsolationLevel level, boolean autoCommit) {
		Future<SqlSession> future = Future.future();
		client.getConnection(c -> {
			if (c.succeeded()) {
				SQLConnection conn = c.result();
				conn.setAutoCommit(false, res -> {
					if (res.succeeded()) {
						Transaction tx = new JdbcTransaction(conn);
						log.debug("set auto commit false, transaction begin");
						log.debug("create new session by connection: " + conn);
						future.complete(new DefaultSqlSession(tx));
					} else {
						future.fail(res.cause());
					}
				});

			} else {
				future.fail(c.cause());
			}
		});
		return future;
	}

	@Override
	public SqlSession currentSession() {
		return (SqlSession) threadLocal.get();
	}
}
