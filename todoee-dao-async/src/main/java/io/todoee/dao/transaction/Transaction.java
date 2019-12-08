package io.todoee.dao.transaction;

import io.vertx.core.Future;
import io.vertx.ext.sql.SQLConnection;

/**
 * Wraps a database connection. Handles the connection lifecycle that comprises:
 * its creation, preparation, commit/rollback and close.
 *
 * @author James.zhang
 */
public interface Transaction {

	/**
	 * Retrieve inner database connection
	 * 
	 * @return DataBase connection
	 */
	SQLConnection getConnection();

//	boolean autoCommit();

	/**
	 * Commit inner database connection.
	 */
	Future<Void> commit();

	/**
	 * Rollback inner database connection.
	 */
	Future<Void> rollback();

	/**
	 * Close inner database connection.
	 */
	Future<Void> close();

}
