package io.todoee.dao.session;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import io.todoee.dao.transaction.JdbcTransaction;
import io.todoee.dao.transaction.Transaction;
import io.todoee.dao.transaction.TransactionIsolationLevel;

/**
 * @author James.zhang
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

	@Inject
	private DataSource dataSource;

	private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<>();

	public SqlSession openSession() {
		return openSession(true);
	}

	public SqlSession openSession(boolean autoCommit) {
		SqlSession session = (SqlSession) threadLocal.get();
		if (session == null) {
			session = openSessionFromDataSource(null, autoCommit);
			threadLocal.set(session);
		}
		return session;
	}
	
	@Override
	public void closeSession() {
		SqlSession session = (SqlSession) threadLocal.get();
		if (session != null) {
			session.close();
		}
		threadLocal.remove();
	}

	private SqlSession openSessionFromDataSource(
			TransactionIsolationLevel level, boolean autoCommit) {
		Transaction tx = null;
		try {
			tx = new JdbcTransaction(dataSource, level, autoCommit);
			return new DefaultSqlSession(tx);
		} catch (Exception e) {
			// may have fetched a connection so lets call
			// close()
			closeTransaction(tx); 
			throw new RuntimeException("Error opening session.  Cause: ", e);
		}
	}

	private void closeTransaction(Transaction tx) {
		if (tx != null) {
			try {
				tx.close();
			} catch (SQLException ignore) {
				// Intentionally ignore. Prefer previous error.
			}
		}
	}

}
