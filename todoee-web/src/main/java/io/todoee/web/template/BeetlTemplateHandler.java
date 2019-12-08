package io.todoee.web.template;

import javax.inject.Inject;

import io.todoee.web.constants.WebConstant;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.TemplateHandler;

/**
 *
 * @author James.zhang
 */
public class BeetlTemplateHandler implements TemplateHandler {

	private final TemplateEngine engine;
	private final String contentType;
	private String indexTemplate;

	@Inject
	public BeetlTemplateHandler(TemplateEngine engine) {
		this(engine, DEFAULT_CONTENT_TYPE);
	}
	
	public BeetlTemplateHandler(TemplateEngine engine, String contentType) {
		this.engine = engine;
		this.contentType = contentType;
		this.indexTemplate = DEFAULT_INDEX_TEMPLATE;
	}

	@Override
	public void handle(RoutingContext context) {
		
		String file;
		Object returned = context.get(WebConstant.METHOD_RETURN_OBJ);
		if (returned != null) {
			file = (String)returned;
		} else {
			file = context.normalisedPath();
		}
		if (file.endsWith("/") && null != indexTemplate) {
//			file += indexTemplate;
			file = file.substring(0, file.lastIndexOf("/"));
		}
		engine.render(
				context.data(),
				file,
				res -> {
					if (res.succeeded()) {
						context.response()
								.putHeader(HttpHeaders.CONTENT_TYPE,
										contentType).end(res.result());
					} else {
						context.fail(res.cause());
					}
				});
	}

	@Override
	public TemplateHandler setIndexTemplate(String indexTemplate) {
		this.indexTemplate = indexTemplate;
		return this;
	}
}
