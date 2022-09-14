package com.mszlu.spring.beans.factory.config;

import com.mszlu.spring.beans.MutablePropertyValues;

/**
 * bean定义器:描述bean的属性
 */
public interface BeanDefinition {

    MutablePropertyValues getPropertyValues();

    /**
     * Return if there are property values defined for this bean.
     * @since 5.0.2
     */
    default boolean hasPropertyValues() {
        return !getPropertyValues().isEmpty();
    }


    /**
     * 设置bean的className
     * @param beanClassName
     */
    void setBeanClassName(String beanClassName);


    String getBeanClassName();

    /**
     * 设置bean的作用域
     * @param scope
     */
    void setScope(String scope);

    /**
     * 设置当前bean依赖的其他bean名称
     * @param dependsOn
     */
    void setDependsOn(String... dependsOn);

    /**
     * 是否单例
     * @return
     */
    boolean isSingleton();

    /**
     * 是否非单例
     * @return
     */
    boolean isPrototype();

    /**
     * 指定使用的工厂bean，如果有的话
     * @param factoryBeanName
     */
    void setFactoryBeanName(String factoryBeanName);
}