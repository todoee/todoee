package io.todoee.core.exception;

import io.todoee.core.context.IocContext;
import io.todoee.core.lang.LangMessageFinder;
import lombok.extern.slf4j.Slf4j;

/**
 * exception factory
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class ExceptionFactory {
	
	public static void throwEx(String errCode, Object... objs) {
		int failureCode = Integer.parseInt(errCode);
		throwEx(failureCode, objs);
	}
	
	public static void throwEx(int errCode, Object... objs) {
		String message = IocContext.getBean(LangMessageFinder.class).getMessage(errCode, objs);
		log.error("throw exception: " + errCode + "::" + message);
		ServiceException se = new ServiceException(errCode, message);
		throw se;
	}
	
	

}
