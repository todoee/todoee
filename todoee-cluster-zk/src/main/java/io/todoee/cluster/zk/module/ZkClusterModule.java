package io.todoee.cluster.zk.module;

import com.google.inject.AbstractModule;

import io.todoee.cluster.zk.config.ZkClusterConfig;
import io.todoee.cluster.zk.provider.ZkClusterProvider;
import io.vertx.core.spi.cluster.ClusterManager;

/**
 * 
 * @author James.zhang
 *
 */
public class ZkClusterModule extends AbstractModule {
	
	@Override
    protected void configure() {
    	bind(ClusterManager.class).toProvider(ZkClusterProvider.class).asEagerSingleton();
    	bind(ZkClusterConfig.class).asEagerSingleton();
    }
}
