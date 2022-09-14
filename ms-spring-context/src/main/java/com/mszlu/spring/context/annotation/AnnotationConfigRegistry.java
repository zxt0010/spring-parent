package com.mszlu.spring.context.annotation;

public interface AnnotationConfigRegistry {
  //根据class 进行注册
   void register(Class<?>... componentClasses);
  //根据包名 扫描指定的注解
   void scan(String... basePackages);

}