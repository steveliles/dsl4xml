package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

public interface NamedObject<T> extends Content<T> {

    public <I> NamedObject<I> via(Class<I> anIntermediateType);

    public <I> NamedObject<I> via(Class<I> anIntermediateType, Converter<I,T> aConverter);

    public NamedObject<T> with(Content<?> ... aContent);

}
