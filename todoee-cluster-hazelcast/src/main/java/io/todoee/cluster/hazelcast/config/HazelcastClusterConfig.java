package io.todoee.cluster.hazelcast.config;

import javax.inject.Inject;

import com.typesafe.config.Config;

import io.todoee.core.config.DoConfig;
import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Data
public class HazelcastClusterConfig {
	
	private String zkHosts;
	private long zkRetryInitialSleepTime;
	private Integer zkRetryMaxTimes;
	
	public static final String CLUSTER_ZK_HOSTS_KEY = "cluster.zk.hosts";
	public static final String CLUSTER_ZK_RETRY_SLEEP_TIME_KEY = "cluster.zk.retry.initialSleepTime";
	public static final String CLUSTER_ZK_RETRY_MAX_TIMES_KEY = "cluster.zk.retry.maxTimes";
	
	private static final String DEFAULT_ZK_HOSTS = "0.0.0.0:2181";
	private static final long DEFAULT_ZK_RETRY_SLEEP_TIME = 2000;
	private static final int DEFAULT_ZK_RETRY_MAX_TIMES = 1;
	
	@Inject
	public HazelcastClusterConfig(DoConfig doConfig) {
		Config config = doConfig.todoee();
		if (config.hasPath(CLUSTER_ZK_HOSTS_KEY)) {
			this.setZkHosts(config.getString(CLUSTER_ZK_HOSTS_KEY));
		} else {
			this.setZkHosts(DEFAULT_ZK_HOSTS);
		}
		if (config.hasPath(CLUSTER_ZK_RETRY_SLEEP_TIME_KEY)) {
			this.setZkRetryInitialSleepTime(config.getLong(CLUSTER_ZK_RETRY_SLEEP_TIME_KEY));
		} else {
			this.setZkRetryInitialSleepTime(DEFAULT_ZK_RETRY_SLEEP_TIME);
		}
		if (config.hasPath(CLUSTER_ZK_RETRY_MAX_TIMES_KEY)) {
			this.setZkRetryMaxTimes(config.getInt(CLUSTER_ZK_RETRY_MAX_TIMES_KEY));
		} else {
			this.setZkRetryMaxTimes(DEFAULT_ZK_RETRY_MAX_TIMES);
		}
	}
}
