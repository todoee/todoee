package io.todoee.cluster.hazelcast.factory;

import com.google.inject.Module;

import io.todoee.cluster.hazelcast.module.HazelcastClusterModule;
import io.todoee.core.spi.ClusterFactory;

public class HazelcastClusterFactory implements ClusterFactory {

	@Override
	public Module get() {
		return new HazelcastClusterModule();
	}

}
