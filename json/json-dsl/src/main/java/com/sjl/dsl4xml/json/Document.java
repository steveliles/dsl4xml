package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

public interface Document<T> extends Definition<T> {

    public Document<T> with(Content<?> ... aContent);

    public <I> Document<I> via(Class<I> anIntermediateType);

    public <I> Document<I> via(Class<I> anIntermediateType, Converter<I,? extends T> aConverter);

}
