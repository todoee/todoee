package io.todoee.dao.session;

import java.util.List;

import io.todoee.dao.transaction.Transaction;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class DefaultSqlSession implements SqlSession {

	protected Transaction transaction;
	protected SQLConnection connection;

	public DefaultSqlSession(SQLConnection connection) {
		this.connection = connection;
	}

	public DefaultSqlSession(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public Future<Void> commit() {
		Future<Void> future = Future.future();
		transaction.commit().setHandler(future);
		return future;
	}

	@Override
	public Future<Void> rollback() {
		Future<Void> future = Future.future();
		transaction.rollback().setHandler(future);
		return future;
	}

	@Override
	public Future<Void> close() {
		Future<Void> future = Future.future();
		if (transactional()) {
			transaction.close().setHandler(future);
		} else {
			connection.close(future);
		}
		return future;
	}

	@Override
	public SQLConnection getConnection() {
		if (transaction != null) {
			return this.transaction.getConnection();
		} else {
			return connection;
		}
	}

	@Override
	public Future<Integer> update(String sql, JsonArray params) {
		Future<Integer> future = Future.future();
		SQLConnection connection = getConnection();
		log.debug("execute sql:{}", sql);
		log.debug("params:{}", params);
		long current = System.currentTimeMillis();
		connection.updateWithParams(sql, params, res -> {
			if (res.succeeded()) {
				Integer num = res.result().getUpdated();
				log.debug("execute update num: [" + num + "], cost: " + (System.currentTimeMillis() - current)
						+ "ms, sql: " + sql);
				future.complete(num);
			} else {
				future.fail(res.cause());
			}
		});
		return future;
	}

	@Override
	public Future<Long> count(String sql, JsonArray params) {
		Future<Long> future = Future.future();
		get(sql, params).setHandler(jo -> {
			if (jo.succeeded()) {
				future.complete((Long) jo.result().iterator().next().getValue());
			} else {
				future.fail(jo.cause());
			}
		});
		return future;
	}

	@Override
	public Future<JsonObject> get(String sql, JsonArray params) {
		Future<JsonObject> future = Future.future();
		list(sql, params).setHandler(a -> {
			if (a.succeeded()) {
				JsonArray ja = a.result();
				if (ja.size() == 1) {
					log.debug("result: " + ja.getJsonObject(0));
					future.complete(ja.getJsonObject(0));
				} else if (ja.size() == 0) {
					log.debug("result num is 0");
					future.complete();
				} else {
					future.fail("Query out multiple results!");
				}
			} else {
				future.fail(a.cause());
			}
		});
		return future;
	}

	@Override
	public Future<JsonArray> list(String sql, JsonArray params) {
		Future<JsonArray> future = Future.future();
		SQLConnection connection = getConnection();
		log.debug("execute sql:{}", sql);
		log.debug("params:{}", params);
		long current = System.currentTimeMillis();
		connection.queryStreamWithParams(sql, params, res -> {
			if (res.succeeded()) {
				log.debug("execute select cost: " + (System.currentTimeMillis() - current) + "ms, sql: " + sql);
				dealResult(res.result()).setHandler(v -> {
					if (v.succeeded()) {
						future.complete(v.result());
					} else {
						future.fail(v.cause());
					}
				});
			} else {
				future.fail(res.cause());
			}
		});
		return future;
	}

	private Future<JsonArray> dealResult(SQLRowStream streams) {
		Future<JsonArray> future = Future.future();
		JsonArray result = new JsonArray();
		List<String> columnList = streams.columns();
		streams.handler(array -> {
			JsonObject obj = new JsonObject();
			for (int i = 0; i < columnList.size(); i++) {
				obj.put(columnList.get(i).toLowerCase(), array.getValue(i));
			}
			result.add(obj);
			future.complete(result);
		});
		return future;
	}

	@Override
	public boolean transactional() {
		return this.transaction != null;
	}
}
