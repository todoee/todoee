package io.todoee.quartz.module;
//package io.meedo.dao.module;
//
//import java.lang.reflect.InvocationHandler;
//
//import javax.sql.DataSource;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.matcher.Matchers;
//
//import io.meedo.base.utils.PackageScanner;
//import io.meedo.dao.DaoProxyProvider;
//import io.meedo.dao.annotation.Job;
//import io.meedo.dao.annotation.Transactional;
//import io.meedo.dao.connection.DataSourceProvider;
//import io.meedo.dao.proxy.DaoInvocationHandler;
//import io.meedo.dao.session.DefaultSqlSessionFactory;
//import io.meedo.dao.session.SqlSessionFactory;
//import io.meedo.dao.transaction.TransactionAop;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class QuartzModule extends AbstractModule {
//	
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//    protected void configure() {
//    	bind(SqlSessionFactory.class).toInstance(new DefaultSqlSessionFactory());
//    	TransactionAop transactionAop = new TransactionAop();
//    	bind(TransactionAop.class).toInstance(transactionAop);
//    	bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionAop);
//    	bind(InvocationHandler.class).to(DaoInvocationHandler.class).asEagerSingleton();
//    	for (Class clazz : PackageScanner.scanAnnotation(Job.class)) {
//    		log.debug("Found Table Class: " + clazz.getName());
//    		bind(clazz).toProvider(DaoProxyProvider.of(clazz)).asEagerSingleton();
//		}
//    }
//}
