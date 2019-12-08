package io.todoee.faces.core.util;

import javax.el.ELContext;
import javax.faces.context.FacesContext;

/**
 * 
 * @author James.zhang
 *
 */
public class MBeanFinder {
	
	@SuppressWarnings("unchecked")
	public static <T> T lookup(Class<T> clazz, String beanName) {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		ELContext elContext = facesContext.getELContext();
		
		T resultBean = (T) facesContext.getApplication().getExpressionFactory()
				.createValueExpression(elContext, "#{" + beanName + "}", clazz)
				.getValue(elContext);

		return resultBean;
	}
	
	public static <T> T lookup(Class<T> clazz) {
		String beanName = (clazz.getSimpleName().charAt(0) + "").toLowerCase() 
				+ clazz.getSimpleName().substring(1);
		return lookup(clazz, beanName);
	}
	
}
