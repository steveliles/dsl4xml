package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class NumberDoubleConverter implements TypeSafeConverter<Number,Double> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return
            aClass.isAssignableFrom(Double.class) || // object wrapper
            aClass.isAssignableFrom(Double.TYPE);    // primitive
    }

    @Override
    public Double convert(Number aFrom) {
        if (aFrom == null)
            return null;

        if (aFrom instanceof Double)
            return (Double) aFrom;

        return aFrom.doubleValue();
    }
}
