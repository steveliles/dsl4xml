package com.sjl.dsl4xml;

public interface Converter<F,T> {

    public boolean canConvertFrom(Class<?> aClass);

    public boolean canConvertTo(Class<?> aClass);

    public T convert(F aFrom);

}
