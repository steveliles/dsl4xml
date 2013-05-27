package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class NumberIntegerConverter implements TypeSafeConverter<Number,Integer> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return
            aClass.isAssignableFrom(Integer.class) || // object wrapper
            aClass.isAssignableFrom(Integer.TYPE);    // primitive
    }

    @Override
    public Integer convert(Number aFrom) {
        if (aFrom == null)
            return null;

        if (aFrom instanceof Integer)
            return (Integer) aFrom;

        return aFrom.intValue();
    }
}
