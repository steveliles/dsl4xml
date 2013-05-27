package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class NumberShortConverter implements TypeSafeConverter<Number,Short> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return
            aClass.isAssignableFrom(Short.class) || // object wrapper
            aClass.isAssignableFrom(Short.TYPE);    // primitive
    }

    @Override
    public Short convert(Number aFrom) {
        if (aFrom == null)
            return null;

        if (aFrom instanceof Short)
            return (Short)aFrom;

        return aFrom.shortValue();
    }
}
