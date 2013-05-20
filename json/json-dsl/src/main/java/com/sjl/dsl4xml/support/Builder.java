package com.sjl.dsl4xml.support;

// TODO - extract to dsl4 support
public interface Builder<T> {

    public void prepare();

    public void setValue(String aName, Object aValue);

    public T build();

}
