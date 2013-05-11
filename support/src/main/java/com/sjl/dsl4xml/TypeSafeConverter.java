package com.sjl.dsl4xml;

public interface TypeSafeConverter<F,T> extends Converter<F,T> {

    public boolean canConvertFrom(Class<?> aClass);

    public boolean canConvertTo(Class<?> aClass);

}
