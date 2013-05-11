package com.sjl.dsl4xml;

public interface Converter<F,T> {

    public T convert(F aFrom);

}
