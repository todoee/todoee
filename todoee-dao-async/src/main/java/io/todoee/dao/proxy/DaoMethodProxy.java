package io.todoee.dao.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import io.todoee.dao.annotation.Delete;
import io.todoee.dao.annotation.Insert;
import io.todoee.dao.annotation.Select;
import io.todoee.dao.annotation.Update;
import lombok.Data;

/**
 * 
 * @author James.zhang
 *
 */
@Data
public class DaoMethodProxy {
	private String id;
	private Class<? extends Annotation> operate;
	private String sql;
	private Class<?> paramType;
	private Class<?> returnType;
	
	public DaoMethodProxy(Method method) {
		this.id = method.getDeclaringClass().getName() + "." + method.getName();
		parseMethod(method);
	}
	
	private void parseMethod(Method method) {
    	Parameter[] parameters = method.getParameters();
    	if (parameters.length > 1) {
    		throw new RuntimeException("method " + method.getName() + " args count > 1");
		}
    	
    	String sqlStr;
    	Class<? extends Annotation> operate;
		if (method.isAnnotationPresent(Select.class)) {
			sqlStr = method.getDeclaredAnnotation(Select.class).value();
			operate = Select.class;
		} else if (method.isAnnotationPresent(Insert.class)) {
			sqlStr = method.getDeclaredAnnotation(Insert.class).value();
			operate = Insert.class;
		} else if (method.isAnnotationPresent(Update.class)) {
			sqlStr = method.getDeclaredAnnotation(Update.class).value();
			operate = Update.class;
		} else if (method.isAnnotationPresent(Delete.class)) {
			sqlStr = method.getDeclaredAnnotation(Delete.class).value();
			operate = Delete.class;
		} else {
			throw new RuntimeException("method " + method.getName() + " annotation not found");
		}
		
		setSql(sqlStr);
		setOperate(operate);
		setReturnType(method.getReturnType());
		setParamType(parameters[0].getType());
    }
}
