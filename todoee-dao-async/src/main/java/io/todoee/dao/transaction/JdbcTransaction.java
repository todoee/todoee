package io.todoee.dao.transaction;

import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author James.zhang
 */
@Slf4j
public class JdbcTransaction implements Transaction {

	protected SQLConnection connection;

	public JdbcTransaction(SQLConnection connection) {
		this.connection = connection;
	}

	@Override
	public SQLConnection getConnection() {
		return connection;
	}

	@Override
	public Future<Void> commit() {
		Future<Void> future = Future.future();
		log.debug("Committing JDBC Connection [" + connection + "]");
		connection.commit(future);
		return future;
	}

	@Override
	public Future<Void> rollback() {
		Future<Void> future = Future.future();
		log.debug("Rolling back JDBC Connection [" + connection + "]");
		connection.rollback(future);
		return future;
	}

	@Override
	public Future<Void> close() {
		Future<Void> future = Future.future();
		log.debug("Closing JDBC Connection [" + connection + "]");
		connection.close(future);
		return future;
	}

}
