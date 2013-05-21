package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Converter;

public class PropertyBuilder<T> implements Builder<T> {

    private Converter<String,T> converter;
    private String prepared;

    @Override
    public void prepare() {
        // TODO
    }

    @Override
    public void setValue(String aName, Object aValue) {
        // TODO
    }

    @Override
    public T build() {
        return null;  // TODO
    }
}
