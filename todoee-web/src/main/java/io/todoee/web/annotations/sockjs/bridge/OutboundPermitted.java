package io.todoee.web.annotations.sockjs.bridge;

import java.lang.annotation.*;

@Repeatable(OutboundsPermitted.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OutboundPermitted {

  String address() default "";

  String addressRegex() default "";

  String requiredAuthority() default "";
}
