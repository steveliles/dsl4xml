package com.sjl.dsl4xml.json;

public interface Builder<T> {

    public void prepare();

    public void setValue(String aName, Object aValue);

    public T build();

}
