package io.todoee.cluster.hazelcast.provider;

import com.google.inject.Provider;

import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 *
 * @author James.zhang
 */
public class HazelcastClusterProvider implements Provider<ClusterManager> {

    @Override
    public ClusterManager get() {
		return new HazelcastClusterManager();
    }
}
