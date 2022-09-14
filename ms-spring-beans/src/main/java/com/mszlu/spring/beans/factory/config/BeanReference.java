package com.mszlu.spring.beans.factory.config;

/**
 *
 */
public interface BeanReference {

    /**
     * Return the target bean name that this reference points to (never {@code null}).
     */
    String getBeanName();
}
