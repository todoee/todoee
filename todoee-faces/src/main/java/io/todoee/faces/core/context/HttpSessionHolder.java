package io.todoee.faces.core.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Session的信息
 * 
 * @author James.zhang
 * 
 * @version 1.0
 */
public class HttpSessionHolder {

	/**
	 * 向session添加参数
	 * 
	 * @param name
	 *            参数名
	 * 
	 * @param value
	 *            参数值
	 */
	public static void setAttribute(String name, Object value) {
		getExternalContext().getSessionMap().put(name, value);
	}

	/**
	 * 获得session中参数值
	 * 
	 * @param name
	 *            参数名
	 * 
	 * @return 参数值
	 */
	public static Object getAttribute(String name) {
		return getExternalContext().getSessionMap().get(name);
	}

	public static void clearAllAttribute() {
		getExternalContext().getSessionMap().clear();
	}

	/**
	 * 移除session中参数值
	 * 
	 * @param name
	 *            参数名
	 * 
	 * @return 参数值
	 */
	public static void removeAttribute(String name) {
		getExternalContext().getSessionMap().remove(name);
	}
	
	public static void invalidateSession() {
		getExternalContext().invalidateSession();
	}
	
	public static HttpSession getHttpSession() {
		return (HttpSession)getExternalContext().getSession(false);
	}
	
	public static Boolean hasCurrentSession() {
		HttpSession httpSession = (HttpSession)getExternalContext().getSession(false);
		if (httpSession == null || httpSession.isNew()) {
			return false;
		}
		
		return true;
	}
	
	private static ExternalContext getExternalContext() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return externalContext;
	}
}
