package io.todoee.faces.server;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;

import io.todoee.core.context.IocContext;
import io.todoee.faces.config.FacesConfig;
import io.todoee.faces.core.util.WebXmlReader;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class EmbeddedServer {

	private final static String FRAGMENT_CONFIGURATION = "META-INF/web-fragment.xml";

	private Undertow undertow;

	private String host;
	private int port;
	private String contextPath;

	private DeploymentInfo deploymentInfo;

	public DeploymentInfo getDeploymentInfo() {
		return deploymentInfo;
	}

	@Inject
	public EmbeddedServer(FacesConfig config) {
		this(config.getHost(), config.getPort(), config.getContextPath());
	}
	
	public EmbeddedServer(String host, int port, String contextPath) {
		this.host = host;
		this.port = port;
		this.contextPath = contextPath;
		this.deploymentInfo = Servlets.deployment().setClassLoader(
				this.getClass().getClassLoader());
	}

	public void start(Future<Void> startFuture) {
		IocContext.getBean(Vertx.class).executeBlocking(future -> {
			long start = System.currentTimeMillis();
			List<URL> webFragments = getFragmentList();
			webFragments.stream().forEach(webFragment -> {
				InputStream in;
				try {
					in = webFragment.openStream();
				} catch (Exception e) {
					startFuture.fail(e);
					throw new RuntimeException(e);
				}
				WebXmlReader.readWebXml(in, deploymentInfo);
			});

			addDeploymentInfo("todoee-faces", contextPath);

			undertow = Undertow.builder()
					.addHttpListener(port, host)
					.setHandler(deployHandler())
					.build();
			undertow.start();
			future.complete((System.currentTimeMillis() - start));
		}, res -> {
			if (res.succeeded()) {
				log.info("Faces Server(" + host + ":" + port + ") startup in " + res.result() + " ms");
				startFuture.complete();
			} else {
				startFuture.fail(res.cause());
			}
		});
	}

	public void stop() {
		undertow.stop();
	}

	private HttpHandler deployHandler() {
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(
				deploymentInfo);
		manager.deploy();
		
		try {
			return manager.start();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}

	private void addDeploymentInfo(String name, String contextPath) {
		deploymentInfo.setDeploymentName(name);

		ResourceManager c = new ClassPathResourceManager(
				deploymentInfo.getClassLoader(), "META-INF/resources");
		deploymentInfo.setResourceManager(c);

		deploymentInfo.setContextPath(contextPath);
		deploymentInfo.addWelcomePages("index.html", "index.xhtml");
	}

	private List<URL> getFragmentList() {
		List<URL> urlList = new ArrayList<>();
		Enumeration<URL> urls;
		try {
			urls = this.getClass().getClassLoader()
					.getResources(FRAGMENT_CONFIGURATION);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				urlList.add(url);
				log.info("Load Web Fragment: " + url);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return urlList;
	}
}
