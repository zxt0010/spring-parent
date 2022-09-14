package com.mszlu.spring.beans;

import com.mszlu.spring.utils.ObjectUtils;

import java.io.Serializable;


public class PropertyValue implements Serializable {

    private final String name;

    private final Object value;

    public PropertyValue(String name,Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Return the name of the property.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the value of the property.
     * <p>Note that type conversion will <i>not</i> have occurred here.
     * It is the responsibility of the BeanWrapper implementation to
     * perform type conversion.
     */
    public Object getValue() {
        return this.value;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PropertyValue)) {
            return false;
        }
        PropertyValue otherPv = (PropertyValue) other;
        return (this.name.equals(otherPv.name) &&
                ObjectUtils.nullSafeEquals(this.value, otherPv.value));
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
    }

    @Override
    public String toString() {
        return "bean property '" + this.name + "'";
    }
}
