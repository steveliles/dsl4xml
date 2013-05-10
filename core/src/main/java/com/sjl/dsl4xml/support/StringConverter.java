package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Converter;

public abstract class StringConverter<T> implements Converter<String,T> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return aClass.isAssignableFrom(CharSequence.class);
    }
}
