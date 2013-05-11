package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.TypeSafeConverter;

public abstract class StringConverter<T> implements TypeSafeConverter<String,T> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return CharSequence.class.isAssignableFrom(aClass);
    }
}
