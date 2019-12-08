package io.todoee.faces.core.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * JSF Message Class
 * 
 * @author James.zhang
 *
 */
public class FacesMsg {

	private static final String WARN_MSG = "提示信息";
	private static final String ERROR_MSG = "错误信息";
	private static final String INFO_MSG = "成功信息";

	public static void info(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				INFO_MSG, message);
		FacesContext.getCurrentInstance().addMessage("", msg);
	}
	public static void warn(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
				WARN_MSG, message);
		FacesContext.getCurrentInstance().addMessage("", msg);
	}
	public static void error(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				ERROR_MSG, message);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
