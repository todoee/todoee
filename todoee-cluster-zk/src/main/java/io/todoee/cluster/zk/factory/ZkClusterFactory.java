package io.todoee.cluster.zk.factory;

import com.google.inject.Module;

import io.todoee.cluster.zk.module.ZkClusterModule;
import io.todoee.core.spi.ClusterFactory;

public class ZkClusterFactory implements ClusterFactory {

	@Override
	public Module get() {
		return new ZkClusterModule();
	}

}
