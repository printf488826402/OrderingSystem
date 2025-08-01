package com.sky.annotation;

import com.sky.enumeration.OperationType;
import org.apache.poi.ss.formula.functions.T;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于表示需要字段自动填充的方法
 */
@Target(ElementType.METHOD)// 表示该注解用于方法上
@Retention(RetentionPolicy.RUNTIME)// 表示该注解在运行时生效
public @interface AutoFill {
    //数据库操作类型UPDATTE INSERT
    OperationType value();
}
