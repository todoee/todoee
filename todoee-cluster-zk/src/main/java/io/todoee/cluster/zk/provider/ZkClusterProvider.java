package io.todoee.cluster.zk.provider;

import javax.inject.Inject;

import com.google.inject.Provider;

import io.todoee.cluster.zk.config.ZkClusterConfig;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;

/**
 *
 * @author James.zhang
 */
public class ZkClusterProvider implements Provider<ClusterManager> {

	@Inject
	private ZkClusterConfig config;
	
    @Override
    public ClusterManager get() {
    	JsonObject zkConfig = new JsonObject();
		zkConfig.put("zookeeperHosts", config.getZkHosts());
		zkConfig.put("rootPath", "io.todoee");
		zkConfig.put("retry",
				new JsonObject().put("initialSleepTime", config.getZkRetryInitialSleepTime())
						.put("maxTimes", config.getZkRetryMaxTimes()));

		return new ZookeeperClusterManager(zkConfig);
    }
}
