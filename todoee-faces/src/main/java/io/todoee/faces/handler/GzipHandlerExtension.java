package io.todoee.faces.handler;

import javax.servlet.ServletContext;

import io.undertow.predicate.Predicates;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;

/**
 * 
 * @author James.zhang
 *
 */
public class GzipHandlerExtension implements ServletExtension {

	@Override
	public void handleDeployment(DeploymentInfo deploymentInfo, ServletContext servletContext) {
		deploymentInfo.addInitialHandlerChainWrapper(handler -> {
			ContentEncodingRepository contentEncodingRepository = new ContentEncodingRepository()
					.addEncodingHandler("gzip", new GzipEncodingProvider(), 50, Predicates.truePredicate());
			EncodingHandler dynamicHandler = new EncodingHandler(contentEncodingRepository).setNext(handler);
			return dynamicHandler;
		});
	}
}