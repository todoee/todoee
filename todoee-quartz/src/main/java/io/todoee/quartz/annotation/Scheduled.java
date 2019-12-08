package io.todoee.quartz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author James.zhang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scheduled {
	String cron() default "";
	int interval() default -1;
	int delayInMillis() default 0;
	TimeUnit unit() default TimeUnit.SECONDS;
}
