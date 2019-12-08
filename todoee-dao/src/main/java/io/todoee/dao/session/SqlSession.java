package io.todoee.dao.session;

import java.io.Closeable;
import java.sql.Connection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * The primary Java interface for working with MyBatis.
 * Through this interface you can execute commands, get mappers and manage transactions.
 *
 * @author James.zhang
 */
public interface SqlSession extends Closeable {

  /**
   * Flushes batch statements and commits database connection.
   * Note that database connection will not be committed if no updates/deletes/inserts were called.
   * To force the commit call {@link SqlSession#commit(boolean)}
   */
  void commit();

  /**
   * Discards pending batch statements and rolls database connection back.
   * Note that database connection will not be rolled back if no updates/deletes/inserts were called.
   * To force the rollback call {@link SqlSession#rollback(boolean)}
   */
  void rollback();

  void close();

  Connection getConnection();
  
  JSONObject get(String sql, Object[] params);
  
  Long count(String sql, Object[] params);
  
  JSONArray list(String sql, Object[] params);
  
  int update(String sql, Object[] params);
  
}
