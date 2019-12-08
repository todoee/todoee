package io.todoee.dao.transaction;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import io.todoee.dao.session.SqlSession;
import io.todoee.dao.session.SqlSessionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 */
@Slf4j
public class TransactionAop implements MethodInterceptor {
	
	@Inject
	private SqlSessionFactory sessionFactory;
	
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
    	log.debug("JDBC Transaction begin");
        Object obj = null;
		SqlSession session = sessionFactory.openSession(false);
        try {
        	obj = invocation.proceed();
        	session.commit();
        } catch (Exception e) {
        	session.rollback();
        }finally{
        	session.close();
        }
        log.debug("JDBC Transaction end");
        return obj;
    }
}
