package io.todoee.httpclient.proxy;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import io.todoee.httpclient.config.HttpClientConfig;

/**
 * 
 * @author James.zhang
 *
 */
@Data
@Slf4j
@Singleton
public class HttpClientProxyFactory {

	private HttpClientConfig config;
	
	private ResteasyClient client;
	
	@Inject
	public HttpClientProxyFactory(HttpClientConfig config) {
		log.info("Init HttpClientProxyFactory");
		this.config = config;
		log.debug("address: " + config.getAddress());
		log.debug("timeout: " + config.getTimeout());
		log.debug("connections: " + config.getConnections());

		HttpClientBuilder cb = HttpClientBuilder.create();
		SSLContextBuilder sslcb = SSLContextBuilder.create();
		SSLContext sslContext = null;
		try {
			sslContext = sslcb.loadTrustMaterial(
					KeyStore.getInstance(KeyStore.getDefaultType()),
					new TrustStrategy() {
						@Override
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();

		} catch (Exception e) {
			throw new RuntimeException("init http client error", e);
		}

		SSLConnectionSocketFactory sslcf = new SSLConnectionSocketFactory(
				sslContext, NoopHostnameVerifier.INSTANCE);
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http",
						PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslcf);

		HttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				registryBuilder.build());
		CloseableHttpClient httpClient = cb.setConnectionManager(connManager)
				.build();
		ApacheHttpClient4Engine httpEngine = new ApacheHttpClient4Engine(
				httpClient);

		this.client = new ResteasyClientBuilder().httpEngine(httpEngine)
				.connectTimeout(config.getTimeout(), TimeUnit.SECONDS)
				.connectionPoolSize(config.getConnections()).build();
	}

	public <T> T get(Class<T> interfaceClass) {
		ResteasyWebTarget target = client.target(config.getAddress());
		T service = target.proxy(interfaceClass);
		return service;
	}
}
