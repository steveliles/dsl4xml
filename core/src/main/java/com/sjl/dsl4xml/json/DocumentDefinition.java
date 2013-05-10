package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

import java.util.List;

public interface DocumentDefinition<T> {

    public Name alias(String aName, String anAlias);

    public Document<T> mapping(Class<T> aType);

    public <I> Document<T> mapping(Class<I> anIntermediateType, Class<T> aTargetType);

    public <I> Document<T> mapping(Class<I> anIntermediateType, Converter<I,T> aConverter);

    public <R> JSONObject<R> object(String aName);

    public <R> JSONObject<R> object(Name aName);

    public <R> JSONObject<R> object(String aName, Class<R> aType);

    public <R> JSONObject<R> object(Name aName, Class<R> aType);

    public <I,R> JSONObject<R> object(String aName, Class<I> anIntermediateType, Class<R> aTargetType);

    public <I,R> JSONObject<R> object(Name aName, Class<I> anIntermediateType, Class<R> aTargetType);

    public <I,R> JSONObject<R> object(String aName, Class<I> anIntermediateType, Converter<I,R> aConverter);

    public <I,R> JSONObject<R> object(Name aName, Class<I> anIntermediateType, Converter<I,R> aConverter);

    public <R> JSONArray<R> array(String aName);

    public <R> JSONArray<R> array(Name aName);

    public <R> JSONArray<R> array(String aName, Converter<List,R> aConverter);

    public <R> JSONArray<R> array(Name aName, Converter<List,R> aConverter);

    public <R> JSONProperty<R> property(Class<R> aClass);

    public <R> JSONProperty<R> property(String aName);

    public <R> JSONProperty<R> property(Name aName);

    public <R> JSONProperty<R> number(String aName);

    public <R> JSONProperty<R> number(Name aName);

    public <R> JSONProperty<R> bool(String aName);

    public <R> JSONProperty<R> bool(Name aName);
}
