package io.todoee.faces.core.util;

import java.io.IOException;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import io.todoee.faces.core.context.HttpRequestHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * JSF Utility Class
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class FacesUtil {

	public static void reloadCurrentPage() {
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		String requestURI = HttpRequestHolder.getRequestURI();
		try {
			ec.redirect(requestURI);
		} catch (IOException e) {
			log.error("reload page '" + requestURI + "' fail", e);
		}
	}

	public static void redirect_1(String page) {
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			String mainPage = ec.getRequestContextPath() + page;
			ec.redirect(mainPage);
		} catch (IOException e) {
			log.error("Redirect page fail with invalid page url, Maybe '"
					+ page + "' is not exist", e);
		}
	}
	
	public static void redirect_2(String page) {
		FacesContext fc = FacesContext.getCurrentInstance();
	    NavigationHandler nh = fc.getApplication().getNavigationHandler();
	    if (page.contains("?")) {
	    	nh.handleNavigation(fc, null, page + "&faces-redirect=true");
		} else {
			nh.handleNavigation(fc, null, page + "?faces-redirect=true");
		}
	    
	}
	
	public static void exeCallback(String script) {
		log.trace("pagination execute js code: " + script);
//		PrimeFaces.current().executeScript(script);
	}

}
