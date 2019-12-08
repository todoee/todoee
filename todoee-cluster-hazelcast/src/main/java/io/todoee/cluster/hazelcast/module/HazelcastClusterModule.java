package io.todoee.cluster.hazelcast.module;

import com.google.inject.AbstractModule;

import io.todoee.cluster.hazelcast.provider.HazelcastClusterProvider;
import io.vertx.core.spi.cluster.ClusterManager;

/**
 * 
 * @author James.zhang
 *
 */
public class HazelcastClusterModule extends AbstractModule {
	
	@Override
    protected void configure() {
    	bind(ClusterManager.class).toProvider(HazelcastClusterProvider.class).asEagerSingleton();
    }
}
