package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

import java.util.List;

public class JsonDocumentDefinition<T> implements DocumentDefinition<T> {

    @Override
    public Name alias(String aName, String anAlias) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Document<T> mapping(Class<? extends T> aType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedObject<R> object(String aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedObject<R> object(Name aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedObject<R> object(String aName, Class<R> aType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedObject<R> object(Name aName, Class<R> aType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> UnNamedObject<R> object(Class<R> aType) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedArray<R> array(String aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedArray<R> array(Name aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedArray<R> array(String aName, Converter<List, R> aConverter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedArray<R> array(Name aName, Converter<List, R> aConverter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> UnNamedArray<R> array() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> UnNamedArray<R> array(Converter<List, R> aConverter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public <R> UnNamedProperty<R> property(Class<R> aClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedProperty<R> property(String aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedProperty<R> property(Name aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedProperty<R> number(String aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedProperty<R> number(Name aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> UnNamedProperty<R> number() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedProperty<R> bool(String aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> NamedProperty<R> bool(Name aName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <R> UnNamedProperty<R> bool() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
