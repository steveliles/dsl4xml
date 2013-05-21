package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.support.Builder;

public interface UnNamedObject<T> extends Content<T>, Definition<T> {

    public <I> UnNamedObject<I> via(Class<I> anIntermediateType);

    public <I> UnNamedObject<I> via(Class<I> anIntermediateType, Converter<I,T> aConverter);

    public UnNamedObject<T> with(Content<?> ... aContent);

}
