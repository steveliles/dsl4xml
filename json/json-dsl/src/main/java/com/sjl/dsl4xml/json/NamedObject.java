package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Content;
import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.Definition;

public interface NamedObject<T> extends Content<T>, Definition<T> {

    public <I> NamedObject<I> via(Class<I> anIntermediateType);

    public <I> NamedObject<I> via(Class<I> anIntermediateType, Converter<I,T> aConverter);

    public NamedObject<T> with(Content<?> ... aContent);

}
