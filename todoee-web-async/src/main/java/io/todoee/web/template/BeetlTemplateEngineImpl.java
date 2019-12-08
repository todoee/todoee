package io.todoee.web.template;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;

import javax.inject.Inject;

import org.beetl.core.Configuration;
import org.beetl.core.Function;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import io.todoee.web.config.WebConfig;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.impl.ConcurrentLRUCache;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class BeetlTemplateEngineImpl
		implements BeetlTemplateEngine {

	protected final ConcurrentLRUCache<String, Template> cache;
	protected String extension;
	
	private GroupTemplate gt;

	private static final String DEFAULT_TEMPLATE_ROOT = "META-INF/resources";

	@Override
	public BeetlTemplateEngineImpl setExtension(String extension) {
		doSetExtension(extension);
		return this;
	}

	@Override
	public BeetlTemplateEngineImpl setMaxCacheSize(int maxCacheSize) {
		this.cache.setMaxSize(maxCacheSize);
		return this;
	}

	@Inject
	private WebConfig config;
	
	public BeetlTemplateEngineImpl() {
	    doSetExtension(DEFAULT_TEMPLATE_EXTENSION);
	    this.cache = new ConcurrentLRUCache<>(DEFAULT_MAX_CACHE_SIZE);
	    
		ClasspathResourceLoader loader = new ClasspathResourceLoader(DEFAULT_TEMPLATE_ROOT);
		Configuration config;
		try {
			config = Configuration.defaultConfiguration();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		config.setPlaceholderStart("#{");
		config.setPlaceholderEnd("}");
		
		ServiceLoader<Function> funs = ServiceLoader.load(Function.class);
		for (Function fun : funs) {
			config.getFnMap().put(captureName(fun.getClass().getSimpleName()), fun.getClass().getName());
			log.debug("add template function: " + fun.getClass().getName());
		}
		
		gt = new GroupTemplate(loader, config);
	}

	protected static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] += 32;
		return String.valueOf(cs);
	}
	
	public static void main(String[] args) {
		System.out.println(captureName("Zhang"));
	}
	
	@Override
	public void render(Map<String, Object> context, 
			String templateFileName, Handler<AsyncResult<Buffer>> handler) {
		log.debug("use view template: " + templateFileName);
		try {
			Template template;
			if (isCachingEnabled()) {
				log.debug("use cache template");
				template = cache.get(templateFileName);
				if (template == null) {
					template = gt.getTemplate(adjustLocation(templateFileName));
					cache.put(templateFileName, template);
				}
			} else {
				template = gt.getTemplate(adjustLocation(templateFileName));
			}

			template.binding(context);
			handler.handle(Future.succeededFuture(Buffer.buffer(template
					.render())));
		} catch (Exception e) {
			handler.handle(Future.failedFuture(e));
		}
	}

	@Override
	public boolean isCachingEnabled() {
		return config.getTemplateCache();
	}

	protected String adjustLocation(String location) {
	    if (!location.endsWith(extension)) {
	      location += extension;
	    }
	    return location;
	  }

	  protected void doSetExtension(String ext) {
	    this.extension = ext.charAt(0) == '.' ? ext : "." + ext;
	  }
}
