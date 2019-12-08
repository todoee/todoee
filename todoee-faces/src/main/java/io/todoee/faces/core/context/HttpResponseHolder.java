package io.todoee.faces.core.context;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Response的信息
 * 
 * @author James.zhang
 * 
 * @version 1.0 
 */
public class HttpResponseHolder {

	/**
	 * 获得response
	 * 
	 * @return response
	 */
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	/**
	 * 获得response页面writer
	 * 
	 * @return 页面writer
	 * 
	 * @throws IOException
	 */
	public static PrintWriter getWriter() throws IOException {
		String contentType = "application/octet-stream;charset=gb2312";
		HttpServletResponse response = getResponse();
		response.setContentType(contentType);
		return response.getWriter();
	}

	/**
	 * 获得response页面writer
	 * 
	 * @param contentType
	 *            response的contentType
	 * 
	 * @return 页面writer
	 * 
	 * @throws IOException
	 */
	public static PrintWriter getWriter(String contentType) throws IOException {
		HttpServletResponse response = getResponse();
		response.setContentType(contentType);
		return response.getWriter();
	}

	public static void setStatus(int status) {
		getExternalContext().setResponseStatus(status);
	}
	
	public static void addHeader(String name, String value) {
		getExternalContext().addResponseHeader(name, value);
	}
	
	private static ExternalContext getExternalContext() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return externalContext;
	}
}
