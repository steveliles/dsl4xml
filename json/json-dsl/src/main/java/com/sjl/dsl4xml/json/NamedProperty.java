package com.sjl.dsl4xml.json;

public interface NamedProperty<V,T> extends Content<T> {

    public T build(V aValue);

}
