package com.mszlu.spring.web.bind.annotation;

import java.lang.annotation.*;
/**
 * 请求参数注解
 *
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}