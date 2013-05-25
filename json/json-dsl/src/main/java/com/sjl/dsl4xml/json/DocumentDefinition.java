package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.support.Name;

import java.util.List;

public interface DocumentDefinition<T> {

    public <F> ConverterRegistration<F,Object> converting(Class<F> aToConvert);

    public Name alias(String aNameInDocument, String aNameInJavaType);

    public Document<? extends T> mapping(Class<? extends T> aType);

    public <R> NamedObject<R> object(String aName);

    public <R> NamedObject<R> object(Name aName);

    public <R> NamedObject<R> object(String aName, Class<? extends R> aType);

    public <R> NamedObject<R> object(Name aName, Class<? extends R> aType);

    public <R> UnNamedObject<R> object(Class<? extends R> aType);

    public <R> NamedArray<R> array(String aName);

    public <R> NamedArray<R> array(Name aName);

    public <R> NamedArray<R> array(String aName, Class<? extends R> aType);

    public <R> NamedArray<R> array(Name aName, Class<? extends R> aType);

    public UnNamedArray<List> array();

    public <R> UnNamedArray<R> array(Class<? extends R> aType);

    public <R> UnNamedProperty<String,R> property(Class<? extends R> aClass);

    public <R> NamedProperty<String,R> property(String aName);

    public <R> NamedProperty<String,R> property(Name aName);

    public <R> NamedProperty<Number,R> number(String aName, Class<R> aType);

    public <R> NamedProperty<Number,R> number(Name aName, Class<R> aType);

    public <R> UnNamedProperty<Number,R> number(Class<R> aType);

    public <R> NamedProperty<Boolean,R> bool(String aName);

    public <R> NamedProperty<Boolean,R> bool(Name aName);

    public <R> NamedProperty<Boolean,R> bool(String aName, Class<R> aType);

    public <R> NamedProperty<Boolean,R> bool(Name aName, Class<R> aType);

    public <R> UnNamedProperty<Boolean,R> bool();

    public <R> UnNamedProperty<Boolean,R> bool(Class<R> aType);
}
