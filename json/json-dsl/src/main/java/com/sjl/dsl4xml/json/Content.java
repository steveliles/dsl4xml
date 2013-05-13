package com.sjl.dsl4xml.json;

public interface Content<T> {

    public Class<? extends T> getTargetType();

    public <I> Class<I> getIntermediateType();

    public Name getName();

}
