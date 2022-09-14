package com.mszlu.spring.beans.factory.config;

/**
 *
 */
public class RuntimeBeanReference implements BeanReference{

    private final String beanName;

    private final Class<?> beanType;

    private final boolean toParent;

    /**
     * Create a new RuntimeBeanReference to the given bean name.
     * @param beanName name of the target bean
     */
    public RuntimeBeanReference(String beanName) {
        this(beanName, false);
    }

    public RuntimeBeanReference(String beanName, boolean toParent) {
        this.beanName = beanName;
        this.beanType = null;
        this.toParent = toParent;
    }

    public RuntimeBeanReference(Class<?> beanType) {
        this(beanType, false);
    }

    public RuntimeBeanReference(Class<?> beanType, boolean toParent) {
        this.beanName = beanType.getName();
        this.beanType = beanType;
        this.toParent = toParent;
    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    public Class<?> getBeanType() {
        return this.beanType;
    }

    public boolean isToParent() {
        return this.toParent;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RuntimeBeanReference )) {
            return false;
        }
        RuntimeBeanReference that = (RuntimeBeanReference) other;
        return (this.beanName.equals(that.beanName) && this.beanType == that.beanType &&
                this.toParent == that.toParent);
    }

    @Override
    public int hashCode() {
        int result = this.beanName.hashCode();
        result = 29 * result + (this.toParent ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return '<' + getBeanName() + '>';
    }
}
