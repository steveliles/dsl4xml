package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class IdentityConverter implements TypeSafeConverter<Object,Object> {

    private Class<?> clss;

    public IdentityConverter(Class<?> aClass) {
        clss = aClass;
    }

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return clss.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return aClass.isAssignableFrom(clss);
    }

    @Override
    public Object convert(Object aFrom) {
        return aFrom;
    }
}
