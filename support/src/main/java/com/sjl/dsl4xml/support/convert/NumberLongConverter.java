package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class NumberLongConverter implements TypeSafeConverter<Number,Long> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return
            (aClass.isAssignableFrom(Long.class)) || // object wrapper
            (aClass.isAssignableFrom(Long.TYPE));    // primitive
    }

    @Override
    public Long convert(Number aFrom) {
        if (aFrom == null)
            return null;

        if (aFrom instanceof Long)
            return (Long) aFrom;

        return aFrom.longValue();
    }
}
