package com.sjl.dsl4xml.xml;

public interface Tag<T> extends Content<T> {

    public Tag<T> withPCData();

    public Tag<T> withPCDataMappedTo(String aFieldName);

    public Tag<T> with(Attributes anAttributes);

    public Tag<T> with(Content<?>... aContent);

    public Tag<T> with(Attributes anAttributes, Content<?>... aContent);

}
