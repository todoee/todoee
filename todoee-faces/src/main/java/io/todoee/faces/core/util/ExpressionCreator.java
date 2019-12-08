package io.todoee.faces.core.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * 
 * @author James.zhang
 *
 */
public class ExpressionCreator {
	
	public static ValueExpression createValueExpression(String el, Class<?> type) {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();
		ExpressionFactory ef = context.getApplication().getExpressionFactory();
		
		ValueExpression valueExpression = ef.createValueExpression(elContext, el, type);
		
		return valueExpression;
	}
	
	public static MethodExpression createMethodExpression(String el) {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();
		ExpressionFactory ef = context.getApplication().getExpressionFactory();
		
		MethodExpression methodExpression = ef.createMethodExpression(
				elContext, el, Void.TYPE, new Class[0]);	
		
		return methodExpression;
	}
	
}
