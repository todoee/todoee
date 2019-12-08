package io.todoee.faces.handler;

import javax.servlet.ServletContext;

import io.undertow.predicate.Predicates;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;

/**
 * 
 * @author James.zhang
 *
 */
public class StaticHandlerExtension implements ServletExtension {

	@Override
	public void handleDeployment(DeploymentInfo deploymentInfo, ServletContext servletContext) {
		deploymentInfo.addInitialHandlerChainWrapper(handler -> {
			ResourceHandler resourceHandler = new ResourceHandler(deploymentInfo.getResourceManager())
					.setCachable(Predicates.prefix("/static"))
					.setCacheTime(600);

			PredicateHandler predicateHandler = new PredicateHandler(Predicates.prefix("/static"), resourceHandler, handler);
			return predicateHandler;
		});
	}
}