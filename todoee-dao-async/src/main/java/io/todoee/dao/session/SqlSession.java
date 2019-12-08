package io.todoee.dao.session;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

/**
 * The primary Java interface for working with MyBatis. Through this interface
 * you can execute commands, get mappers and manage transactions.
 *
 * @author James.zhang
 */
public interface SqlSession {

	boolean transactional();

	Future<Void> commit();

	Future<Void> rollback();

	Future<Void> close();

	SQLConnection getConnection();

	Future<JsonObject> get(String sql, JsonArray params);

	Future<Long> count(String sql, JsonArray params);

	Future<JsonArray> list(String sql, JsonArray params);

	Future<Integer> update(String sql, JsonArray params);

}
