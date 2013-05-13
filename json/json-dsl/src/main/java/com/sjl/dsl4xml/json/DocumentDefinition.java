package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

import java.util.List;

public interface DocumentDefinition<T> {

    public <F> ConverterRegistration<F,Object> converting(Class<F> aToConvert);

    public Name alias(String aNameInDocument, String aNameInJavaType);

    public Document<T> mapping(Class<? extends T> aType);

    public <R> NamedObject<R> object(String aName);

    public <R> NamedObject<R> object(Name aName);

    public <R> NamedObject<R> object(String aName, Class<? extends R> aType);

    public <R> NamedObject<R> object(Name aName, Class<? extends R> aType);

    public <R> UnNamedObject<R> object(Class<? extends R> aType);

    public <R> NamedArray<R> array(String aName);

    public <R> NamedArray<R> array(Name aName);

    public <R> NamedArray<R> array(String aName, Converter<List,? extends R> aConverter);

    public <R> NamedArray<R> array(Name aName, Converter<List,? extends R> aConverter);

    public <R> UnNamedArray<R> array();

    public <R> UnNamedArray<R> array(Converter<List,? extends R> aConverter);

    public <R> UnNamedProperty<R> property(Class<? extends R> aClass);

    public <R> NamedProperty<R> property(String aName);

    public <R> NamedProperty<R> property(Name aName);

    public <R> NamedProperty<R> number(String aName, Class<R> aType);

    public <R> NamedProperty<R> number(Name aName, Class<R> aType);

    public <R> UnNamedProperty<R> number(Class<R> aType);

    public <R> NamedProperty<R> bool(String aName);

    public <R> NamedProperty<R> bool(Name aName);

    public <R> NamedProperty<R> bool(String aName, Class<R> aType);

    public <R> NamedProperty<R> bool(Name aName, Class<R> aType);

    public <R> UnNamedProperty<R> bool();

    public <R> UnNamedProperty<R> bool(Class<R> aType);
}
