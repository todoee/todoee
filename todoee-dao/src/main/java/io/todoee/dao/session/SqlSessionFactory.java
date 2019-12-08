package io.todoee.dao.session;

/**
 * 
 * @author James.zhang
 *
 */
public interface SqlSessionFactory {
	SqlSession openSession();

	SqlSession openSession(boolean autoCommit);
	
	void closeSession();
}
