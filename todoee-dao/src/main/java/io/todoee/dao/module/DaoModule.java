package io.todoee.dao.module;

import java.lang.reflect.InvocationHandler;

import javax.sql.DataSource;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import io.todoee.base.utils.PackageScanner;
import io.todoee.dao.DaoProxyProvider;
import io.todoee.dao.annotation.Table;
import io.todoee.dao.annotation.Transactional;
import io.todoee.dao.connection.DataSourceProvider;
import io.todoee.dao.proxy.DaoInvocationHandler;
import io.todoee.dao.session.DefaultSqlSessionFactory;
import io.todoee.dao.session.SqlSessionFactory;
import io.todoee.dao.transaction.TransactionAop;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class DaoModule extends AbstractModule {
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected void configure() {
//    	bind(DaoConfig.class).asEagerSingleton();
    	bind(DataSource.class).toProvider(DataSourceProvider.class).asEagerSingleton();
    	bind(SqlSessionFactory.class).toInstance(new DefaultSqlSessionFactory());
    	TransactionAop transactionAop = new TransactionAop();
    	bind(TransactionAop.class).toInstance(transactionAop);
    	bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionAop);
    	bind(InvocationHandler.class).to(DaoInvocationHandler.class).asEagerSingleton();
    	for (Class clazz : PackageScanner.scanAnnotation(Table.class)) {
    		log.debug("Found Table Class: " + clazz.getName());
    		bind(clazz).toProvider(DaoProxyProvider.of(clazz)).asEagerSingleton();
		}
    }
}
