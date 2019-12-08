package io.todoee.service.ref.impl;

import com.google.inject.Module;

import io.todoee.core.spi.ReferenceModule;
import io.todoee.service.ref.module.RefModule;

public class ServiceRefModule implements ReferenceModule {

	@Override
	public Module get() {
		return new RefModule();
	}
	
}
