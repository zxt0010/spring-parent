package com.mszlu.spring.context;

import com.mszlu.spring.beans.factory.BeanFactory;
import com.mszlu.spring.core.io.support.ResourcePatternResolver;

/**
 *
 */
public interface ApplicationContext extends BeanFactory, ResourcePatternResolver {

    /**
     * Return the unique id of this application context.
     * @return the unique id of the context, or {@code null} if none
     */
    String getId();

    /**
     * Return a name for the deployed application that this context belongs to.
     * @return a name for the deployed application, or the empty String by default
     */
    String getApplicationName();

    /**
     * Return a friendly name for this context.
     * @return a display name for this context (never {@code null})
     */
    String getDisplayName();

    String[] getBeanDefinitionNames();

}