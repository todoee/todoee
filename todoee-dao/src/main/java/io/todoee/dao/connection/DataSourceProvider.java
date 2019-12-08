package io.todoee.dao.connection;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.inject.Provider;

/**
 *
 * @author James.zhang
 */
@Slf4j
public class DataSourceProvider implements Provider<DataSource> {

    @Override
    public DataSource get() {
    	long current = System.currentTimeMillis();
    	
    	DruidDataSource dataSource;
		try {
			dataSource = (DruidDataSource) DruidDataSourceFactory
					.createDataSource(toProperties());
			dataSource.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        
        log.info("Init DataSource in "
				+ (System.currentTimeMillis() - current) + "ms");
        return dataSource;
    }

    private Properties toProperties() {
    	InputStream is = ClassLoader.getSystemResourceAsStream ("ds.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
			is.close();
		} catch (Exception e) {
			log.error("load ds properties error", e);
		}

		return properties;
	}
}
