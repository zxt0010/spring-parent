package com.mszlu.spring.context.stereotype;

import com.mszlu.spring.context.annotation.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {

	String value() default "";

}