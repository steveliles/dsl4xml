package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class NumberFloatConverter implements TypeSafeConverter<Number,Float> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return
            aClass.isAssignableFrom(Float.class) || // object wrapper
            aClass.isAssignableFrom(Float.TYPE);    // primitive
    }

    @Override
    public Float convert(Number aFrom) {
        if (aFrom == null)
            return null;

        if (aFrom instanceof Float)
            return (Float) aFrom;

        return aFrom.floatValue();
    }
}

