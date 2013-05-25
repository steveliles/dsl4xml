package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.ParsingException;

public class PropertyBuilder<F,T> implements Builder<T> {

    private Name name;
    private Class<? extends T> target;
    private Converter<F,? extends T> converter;

    public PropertyBuilder(Name aName, Class<? extends T> aTarget, Converter<F,? extends T> aConverter) {
        if (aName == null)
            throw new IllegalArgumentException("Must supply a name");
        if (converter == null)
            throw new IllegalArgumentException("Must supply a converter");

        name = aName;
        target = aTarget;
        converter = aConverter;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Class<? extends T> getTargetType() {
        return target;
    }

    @Override
    public Builder<?> moveDown(Context aContext) {
        throw new ParsingException("Nothing can be nested in property " + name + " - definition doesn't match document?");
    }

    @Override
    public final void prepare(Context aContext) {
        // no-op
    }

    @Override
    public void setValue(Context aContext, String aName, Object aValue) {
        aContext.push(aValue);
    }

    @Override
    public T build(Context aContext) {
        return converter.convert((F)aContext.pop());
    }
}
