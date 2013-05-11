package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

import java.util.List;

public interface DocumentDefinition<T> {

    public Name alias(String aNameInDocument, String aNameInJavaType);

    public Document<T> mapping(Class<? extends T> aType);

    public <R> NamedObject<R> object(String aName);

    public <R> NamedObject<R> object(Name aName);

    public <R> NamedObject<R> object(String aName, Class<R> aType);

    public <R> NamedObject<R> object(Name aName, Class<R> aType);

    public <R> UnNamedObject<R> object(Class<R> aType);

    public <R> NamedArray<R> array(String aName);

    public <R> NamedArray<R> array(Name aName);

    public <R> NamedArray<R> array(String aName, Converter<List,R> aConverter);

    public <R> NamedArray<R> array(Name aName, Converter<List,R> aConverter);

    public <R> UnNamedArray<R> array();

    public <R> UnNamedArray<R> array(Converter<List,R> aConverter);

    public <R> UnNamedProperty<R> property(Class<R> aClass);

    public <R> NamedProperty<R> property(String aName);

    public <R> NamedProperty<R> property(Name aName);

    public <R> NamedProperty<R> number(String aName);

    public <R> NamedProperty<R> number(Name aName);

    public <R> UnNamedProperty<R> number();

    public <R> NamedProperty<R> bool(String aName);

    public <R> NamedProperty<R> bool(Name aName);

    public <R> UnNamedProperty<R> bool();
}
