
package com.mszlu.spring.beans.factory.config;

import com.mszlu.spring.beans.BeansException;
import com.mszlu.spring.beans.factory.BeanFactory;

public interface BeanPostProcessor {
	/**
	 * bean初始化之前执行
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * bean初始化之后执行
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	default Object postProcessBeforeInitialization(Object bean, String beanName,BeanFactory beanFactory) throws Exception {
		return bean;
	}

	/**
	 * bean初始化之后执行
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	default Object postProcessAfterInitialization(Object bean, String beanName,BeanFactory beanFactory) throws Exception {
		return bean;
	}

}
