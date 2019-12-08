package io.todoee.core.spi;

import com.google.inject.Module;

public interface ConfigModuleFactory {
	Module get();
}
