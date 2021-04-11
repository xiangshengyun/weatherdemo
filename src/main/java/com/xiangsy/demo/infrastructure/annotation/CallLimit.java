package com.xiangsy.demo.infrastructure.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author: xiangsy
 * @version: 2021-04-09 19:35
 * @description:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CallLimit {
    String value() default "";

    int limit() default 1;
}
