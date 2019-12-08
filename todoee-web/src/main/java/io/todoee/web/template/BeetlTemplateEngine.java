package io.todoee.web.template;

import io.vertx.ext.web.common.template.TemplateEngine;

/**
 * 
 * @author James.zhang
 *
 */
public interface BeetlTemplateEngine extends TemplateEngine {

	/**
	   * Default max number of templates to cache
	   */
	  int DEFAULT_MAX_CACHE_SIZE = 10000;

	  /**
	   * Default template extension
	   */
	  String DEFAULT_TEMPLATE_EXTENSION = "html";

	  /**
	   * Create a template engine using defaults
	   *
	   * @return  the engine
	   */
	  static BeetlTemplateEngine create() {
	    return new BeetlTemplateEngineImpl();
	  }

	  /**
	   * Set the extension for the engine
	   *
	   * @param extension  the extension
	   * @return a reference to this for fluency
	   */
	  BeetlTemplateEngine setExtension(String extension);

	  /**
	   * Set the max cache size for the engine
	   *
	   * @param maxCacheSize  the maxCacheSize
	   * @return a reference to this for fluency
	   */
	  BeetlTemplateEngine setMaxCacheSize(int maxCacheSize);

}
