package com.sjl.dsl4xml.xml;

import com.sjl.dsl4xml.Converter;

public interface DocumentDefinition<T> {

    public Name alias(String aName, String anAlias);

    public NameSpace nameSpace(String aUrl, String aShortName);

    public NameSpace defaultNameSpace(String aUrl, String aShortName);

    public <R> Tag<R> tag(String aTagName);

    public <R> Tag<R> tag(Name aTagName);

    public <R> Tag<R> tag(String aTagName, Class<R> aType);

    public <R> Tag<R> tag(Name aTagName, Class<R> aType);

    public <I,R> Tag<R> tag(String aTagName, Class<I> anIntermediateType, Class<R> aTargetType);

    public <I,R> Tag<R> tag(Name aTagName, Class<I> anIntermediateType, Class<R> aTargetType);

    public <I,R> Tag<R> tag(String aTagName, Class<I> anIntermediateType, Converter<I,R> aConverter);

    public <I,R> Tag<R> tag(Name aTagName, Class<I> anIntermediateType, Converter<I,R> aConverter);

    public Attributes attributes(String... anAttributeNames);

    public Attributes attributes(Name... anAttributeNames);

    public Content pcData(String aFieldName);

}
