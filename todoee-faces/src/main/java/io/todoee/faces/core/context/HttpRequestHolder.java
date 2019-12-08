package io.todoee.faces.core.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Request的信息
 * 
 * @author James.zhang
 * 
 * @version 1.0
 */
public class HttpRequestHolder {

	/**
	 * 设置request属性
	 * 
	 * @param name
	 *            属性名
	 * @param value
	 *            属性值
	 */
	public static void setAttribute(String name, Object value) {
		getExternalContext().getRequestMap().put(name, value);
	}

	/**
	 * 获得request属性值
	 * 
	 * @param name
	 *            属性名
	 * 
	 * @return 属性值
	 */
	public static Object getAttribute(String name) {
		return getExternalContext().getRequestMap().get(name);
	}

	/**
	 * 获得request中页面参数
	 * 
	 * @param name
	 *            参数名
	 * 
	 * @return 参数值
	 */
	public static String getParameter(String name) {
		return getExternalContext().getRequestParameterMap().get(name);
	}

	/**
	 * 移除request中属性
	 * 
	 * @param name
	 *            属性名
	 * 
	 * @return 属性值
	 */
	public static void removeAttribute(String name) {
		getExternalContext().getRequestMap().remove(name);
	}

	public static String getRequestURI() {
		return ((HttpServletRequest) getExternalContext()
				.getRequest()).getRequestURI();
	}
	
	private static ExternalContext getExternalContext() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return externalContext;
	}
}
