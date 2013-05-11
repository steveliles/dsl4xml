package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.TypeSafeConverter;
import com.sjl.dsl4xml.support.convert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonDocumentDefinition<T> implements DocumentDefinition<T> {

    private List<TypeSafeConverter<?,?>> converters;

    private Document<T> document;

    {
        converters = new ArrayList<TypeSafeConverter<?,?>>();

        converters.addAll(Arrays.asList(
            new PrimitiveBooleanStringConverter(),
            new DecimalToByteStringConverter(),
            new DecimalToShortStringConverter(),
            new DecimalToIntStringConverter(),
            new DecimalToLongStringConverter(),
            new PrimitiveCharStringConverter(),
            new PrimitiveFloatStringConverter(),
            new PrimitiveDoubleStringConverter(),
            new BooleanStringConverter(),
            new ByteStringConverter(),
            new ShortStringConverter(),
            new IntegerStringConverter(),
            new LongStringConverter(),
            new CharacterStringConverter(),
            new FloatStringConverter(),
            new DoubleStringConverter(),
            new ClassStringConverter(),
            new StringStringConverter()
        ));
    }

    @Override
    public <F> ConverterRegistration<F,Object> converting(Class<F> aToConvert) {
        return newConverterRegistration(aToConvert);
    }

    @Override
    public Name alias(final String aName, final String anAlias) {
        return new Name() {
            @Override
            public String getName() {
                return aName;
            }

            @Override
            public String getAlias() {
                return anAlias;
            }
        };
    }

    @Override
    public Document<T> mapping(Class<? extends T> aType) {
        if (document != null)
            throw new IllegalStateException("target type is already set - don't call 'mapping' more than once!");

        return document = new Document<T>(){
            @Override
            public Document<T> with(Content<?>... aContent) {
                return null; // todo
            }

            @Override
            public <I> Document<I> via(Class<I> anIntermediateType) {
                return null; // todo
            }

            @Override
            public <I> Document<I> via(Class<I> anIntermediateType, Converter<I, ? extends T> aConverter) {
                return null;  // todo
            }
        };
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

    private <F> ConverterRegistration<F,Object> newConverterRegistration(final Class<F> aFrom) {
        return new ConverterRegistration<F,Object>(){
            private Class<?> to;
            private Converter<F,Object> converter;

            @Override
            public <R> ConverterRegistration<F, R> to(Class<R> aToType) {
                to = aToType;
                return (ConverterRegistration<F,R>)this;
            }

            @Override
            public ConverterRegistration<F, Object> using(Converter<F, Object> aConverter) {
                converter = aConverter;
                return this;
            }

            @Override
            public Converter<F, Object> get() {
                return converter;
            }
        };
    }
}
