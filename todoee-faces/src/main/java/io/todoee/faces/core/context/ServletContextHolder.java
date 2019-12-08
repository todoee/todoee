package io.todoee.faces.core.context;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Application的信息
 * 
 * @author James.zhang
 * 
 * @version 1.0 
 */
public class ServletContextHolder {

	/**
	 * 获得页面Application
	 * 
	 * @return　页面Application
	 */
	public static Application getApplication() {
		ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		return appFactory.getApplication();
	}

	/**
	 * 获得servlet context.
	 * 
	 * @return servlet context.
	 */
	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
	}

	public static void setAttribute(String name, Object value) {
		getServletContext().setAttribute(name, value);
	}

	public static Object getAttribute(String name) {
		return getServletContext().getAttribute(name);
	}
	
	public static String getContextPath() {
		return getServletContext().getContextPath();
	}
	
	public static String getRealPath() {
		return getServletContext().getRealPath("/");
	}
}
