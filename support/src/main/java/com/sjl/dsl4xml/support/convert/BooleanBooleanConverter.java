package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class BooleanBooleanConverter implements TypeSafeConverter<Boolean,Boolean> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Boolean.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return
            aClass.isAssignableFrom(Boolean.class) || // object wrapper
            aClass.isAssignableFrom(Boolean.TYPE);    // primitive type
    }

    @Override
    public Boolean convert(Boolean aFrom) {
        return aFrom;
    }
}
