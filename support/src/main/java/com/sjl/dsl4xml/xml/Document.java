package com.sjl.dsl4xml.xml;

import com.sjl.dsl4xml.Converter;

public interface Document<T> extends Tag<T> {

    public Document<T> mapping(Class<T> aType);

    public <I> Document<T> mapping(Class<I> anIntermediateType, Class<T> aTargetType);

    public <I> Document<T> mapping(Class<I> anIntermediateType, Converter<I,T> aConverter);

}
