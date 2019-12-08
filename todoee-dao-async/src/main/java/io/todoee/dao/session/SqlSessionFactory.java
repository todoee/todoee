package io.todoee.dao.session;

import io.vertx.core.Future;

/**
 * 
 * @author James.zhang
 *
 */
public interface SqlSessionFactory {
	Future<SqlSession> openSession();

	Future<SqlSession> openSession(boolean autoCommit);
	
	Future<Void> closeSession();
	
	SqlSession currentSession();
}
