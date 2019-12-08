package io.todoee.dao.module;

import java.lang.reflect.InvocationHandler;

import javax.inject.Inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

import io.todoee.base.utils.PackageScanner;
import io.todoee.dao.DaoProxyProvider;
import io.todoee.dao.annotation.Table;
import io.todoee.dao.annotation.Transactional;
import io.todoee.dao.config.DaoConfig;
import io.todoee.dao.proxy.DaoInvocationHandler;
import io.todoee.dao.session.DefaultSqlSessionFactory;
import io.todoee.dao.session.SqlSessionFactory;
import io.todoee.dao.transaction.TransactionAop;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider;
import io.vertx.ext.sql.SQLClient;
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

	@Provides
	@Inject
	private SQLClient client(Vertx vertx, DaoConfig conf) {
		JsonObject config = new JsonObject().put("provider_class", HikariCPDataSourceProvider.class.getName())
				.put("jdbcUrl", conf.getUrl()).put("driverClassName", conf.getDriver())
				.put("username", conf.getUsername()).put("password", conf.getPassword())
				.put("maximumPoolSize", conf.getMaximumPoolSize());
		SQLClient client = JDBCClient.createShared(vertx, config);
		return client;
	}
}
