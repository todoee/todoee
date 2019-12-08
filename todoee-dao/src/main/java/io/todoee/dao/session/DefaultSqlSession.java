package io.todoee.dao.session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.todoee.dao.transaction.Transaction;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class DefaultSqlSession implements SqlSession {

	protected Transaction transaction;
	
	public DefaultSqlSession(Transaction transaction) {
		this.transaction = transaction;
	}
	
	@Override
	public void commit() {
		try {
			transaction.commit();
		} catch (SQLException e) {
			throw new RuntimeException("Commit transaction error",e);
		}
	}

	@Override
	public void rollback() {
		try {
			transaction.rollback();
		} catch (SQLException e) {
			throw new RuntimeException("Rollback transaction error",e);
		}
	}

	@Override
	public void close() {
		try {
			transaction.close();
		} catch (SQLException e) {
			throw new RuntimeException("Close transaction error",e);
		}
	}

	@Override
	public Connection getConnection() {
		try {
			return this.transaction.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int update(String sql, Object[] params) {
		PreparedStatement stmt = null;
		try {
			stmt = createPreparedStatement(sql, params);
			long current = System.currentTimeMillis();
			int result = stmt.executeUpdate();
			log.debug("execute total: [" + result + "], cost: " + (System.currentTimeMillis() - current) + "ms, sql: " + sql);
			return result;
		} catch (SQLException e) {
			log.error("execute sql error", e);
			throw new RuntimeException(e);
		} finally {
			close(stmt);
		}
	}

	@Override
	public Long count(String sql, Object[] params) {
		JSONObject obj = get(sql, params);
		return (Long)obj.values().iterator().next();
	}
	
	@Override
	public JSONObject get(String sql, Object[] params) {
		JSONArray list = list(sql, params);
		if (list.size()==1) {
			return list.getJSONObject(0);
		} else if(list.size()==0) {
			return null;
		} else {
			throw new RuntimeException("Query out multiple results!");
		}
	}

	@Override
	public JSONArray list(String sql, Object[] params) {
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			stmt = createPreparedStatement(sql, params);
			long current = System.currentTimeMillis();
			result = stmt.executeQuery();
			log.debug("execute: " + (System.currentTimeMillis() - current) + "ms, sql: " + sql);
			return dealResult(result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(stmt);
			close(result);
		}
	}
	
	private JSONArray dealResult(ResultSet result) throws SQLException {
		ResultSetMetaData rsmd = result.getMetaData();
		int count = rsmd.getColumnCount();
		JSONArray array = new JSONArray();
		while (result.next()) {
			JSONObject returnObj = new JSONObject();
			for (int i = 0; i < count; i++) {
				String columnLabel = rsmd.getColumnLabel(i + 1);
				Object value = result.getObject(i + 1);
				returnObj.put(columnLabel.toLowerCase(), value);
			}
			array.add(returnObj);
		}
		return array;
	}
	
	private PreparedStatement createPreparedStatement(String sql, Object[] params) throws SQLException {
		log.debug("execute sql:{}", sql);
		log.debug("params:{}", params);
		PreparedStatement stmt = getConnection().prepareStatement(sql);
		for (int i = 0; i < params.length; i++) {
			stmt.setObject(i + 1, params[i]);
		}
		return stmt;
	}
	
	private final void close(PreparedStatement x) {
		if (x != null) {
			try {
				x.close();
			} catch (Exception e) {
				log.error("close statement error", e);
			}
		}
	}

	private final void close(ResultSet x) {
		if (x != null) {
			try {
				x.close();
			} catch (Exception e) {
				log.error("close resultset error", e);
			}
		}
	}
	
}
