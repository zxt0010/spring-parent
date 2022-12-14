package com.mszlu.spring.beans.factory.config;

import com.mszlu.spring.utils.ClassUtils;
import com.mszlu.spring.utils.ObjectUtils;

/**
 *
 */
public class TypedStringValue {

    private String value;

    private volatile Object targetType;

    private String specifiedTypeName;

    private volatile boolean dynamic;


    /**
     * Create a new {@link TypedStringValue} for the given String value.
     * @param value the String value
     */
    public TypedStringValue(String value) {
        setValue(value);
    }

    /**
     * Create a new {@link TypedStringValue} for the given String value
     * and target type.
     * @param value the String value
     * @param targetType the type to convert to
     */
    public TypedStringValue(String value, Class<?> targetType) {
        setValue(value);
        setTargetType(targetType);
    }

    /**
     * Create a new {@link TypedStringValue} for the given String value
     * and target type.
     * @param value the String value
     * @param targetTypeName the type to convert to
     */
    public TypedStringValue(String value, String targetTypeName) {
        setValue(value);
        setTargetTypeName(targetTypeName);
    }


    /**
     * Set the String value.
     * <p>Only necessary for manipulating a registered value,
     * for example in BeanFactoryPostProcessors.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Return the String value.
     */
    
    public String getValue() {
        return this.value;
    }

    /**
     * Set the type to convert to.
     * <p>Only necessary for manipulating a registered value,
     * for example in BeanFactoryPostProcessors.
     */
    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }

    /**
     * Return the type to convert to.
     */
    public Class<?> getTargetType() {
        Object targetTypeValue = this.targetType;
        if (!(targetTypeValue instanceof Class)) {
            throw new IllegalStateException("Typed String value does not carry a resolved target type");
        }
        return (Class<?>) targetTypeValue;
    }

    /**
     * Specify the type to convert to.
     */
    public void setTargetTypeName( String targetTypeName) {
        this.targetType = targetTypeName;
    }

    /**
     * Return the type to convert to.
     */
    
    public String getTargetTypeName() {
        Object targetTypeValue = this.targetType;
        if (targetTypeValue instanceof Class) {
            return ((Class<?>) targetTypeValue).getName();
        }
        else {
            return (String) targetTypeValue;
        }
    }

    /**
     * Return whether this typed String value carries a target type .
     */
    public boolean hasTargetType() {
        return (this.targetType instanceof Class);
    }

    /**
     * Determine the type to convert to, resolving it from a specified class name
     * if necessary. Will also reload a specified Class from its name when called
     * with the target type already resolved.
     * @param classLoader the ClassLoader to use for resolving a (potential) class name
     * @return the resolved type to convert to
     * @throws ClassNotFoundException if the type cannot be resolved
     */
    
    public Class<?> resolveTargetType( ClassLoader classLoader) throws ClassNotFoundException {
        String typeName = getTargetTypeName();
        if (typeName == null) {
            return null;
        }
        Class<?> resolvedClass = ClassUtils.forName(typeName, classLoader);
        this.targetType = resolvedClass;
        return resolvedClass;
    }

    /**
     * Set the type name as actually specified for this particular value, if any.
     */
    public void setSpecifiedTypeName( String specifiedTypeName) {
        this.specifiedTypeName = specifiedTypeName;
    }

    /**
     * Return the type name as actually specified for this particular value, if any.
     */
    
    public String getSpecifiedTypeName() {
        return this.specifiedTypeName;
    }

    /**
     * Mark this value as dynamic, i.e. as containing an expression
     * and hence not being subject to caching.
     */
    public void setDynamic() {
        this.dynamic = true;
    }

    /**
     * Return whether this value has been marked as dynamic.
     */
    public boolean isDynamic() {
        return this.dynamic;
    }


    @Override
    public boolean equals( Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TypedStringValue)) {
            return false;
        }
        TypedStringValue otherValue = (TypedStringValue) other;
        return (ObjectUtils.nullSafeEquals(this.value, otherValue.value) &&
                ObjectUtils.nullSafeEquals(this.targetType, otherValue.targetType));
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.targetType);
    }

    @Override
    public String toString() {
        return "TypedStringValue: value [" + this.value + "], target type [" + this.targetType + "]";
    }

}
