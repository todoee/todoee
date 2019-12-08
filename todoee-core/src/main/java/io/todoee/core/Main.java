package io.todoee.core;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.beust.jcommander.JCommander;

import io.todoee.core.boot.CommandParam;
import io.todoee.core.boot.Launcher;
import io.todoee.core.context.IocContext;
import io.todoee.core.spi.ConfigModuleFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class Main {
	public static void main(String[] args) {
		initLog();
		CommandParam options = parseArgs(args);
		initConfig(options);
		Launcher.run();
	}
	
	private static void initLog() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}
	
	private static CommandParam parseArgs(String[] args) {
		CommandParam options = new CommandParam();
		JCommander.newBuilder().addObject(options).build().parse(args);
		log.info("Todoee Boot Args: " + options);
		return options;
	}
	
	private static void initConfig(CommandParam options) {
		IocContext.inject(options);
		ServiceLoader<ConfigModuleFactory> configModules = ServiceLoader.load(ConfigModuleFactory.class);
		Iterator<ConfigModuleFactory> iterator = configModules.iterator();
		if (iterator.hasNext()) {
			ConfigModuleFactory configModuleFactory = iterator.next();
			IocContext.createChildInjector(configModuleFactory.get());
		}
	}
}
