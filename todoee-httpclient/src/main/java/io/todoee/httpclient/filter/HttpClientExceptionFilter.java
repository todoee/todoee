package io.todoee.httpclient.filter;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import io.todoee.httpclient.exception.HttpClientException;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理无返回Exception Rest Filter
 * 
 * @author James.zhang
 * 
 */
@Slf4j
public class HttpClientExceptionFilter implements ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext requestContext,
			ClientResponseContext responseContext) throws IOException {
		log.debug("http response code: " + responseContext.getStatus());
		
		if (responseContext.getStatus() >= 400) {
			HttpClientException e = new HttpClientException(responseContext.getStatus(), "Underlying Device Return Error");
			e.throwException();
		}
	}
}
