package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.TypeSafeConverter;
import com.sjl.dsl4xml.support.StringConverter;
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
        if (document != null)
            throw new IllegalStateException("document already defined - too late to register converters!");

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
    public Document<T> mapping(final Class<? extends T> aType) {
        if (document != null)
            throw new IllegalStateException("target type is already set - don't call 'mapping' more than once!");

        return document = new Document<T>(){
            private Class<?> intermediate;
            private List<Content<?>> content;
            private Converter<?,? extends T> converter;

            @Override
            public Document<T> with(Content<?>... aContent) {
                content = Arrays.asList(aContent);
                return this;
            }

            @Override
            public <I> Document<I> via(Class<I> anIntermediateType) {
                intermediate = anIntermediateType;
                converter = getConverter(anIntermediateType, aType);
                return (Document<I>)this;
            }

            @Override
            public <I> Document<I> via(Class<I> anIntermediateType, Converter<I, ? extends T> aConverter) {
                intermediate = anIntermediateType;
                converter = aConverter;
                return (Document<I>)this;
            }
        };
    }

    @Override
    public <R> NamedObject<R> object(String aName) {
        return object(alias(aName, aName), null);
    }

    @Override
    public <R> NamedObject<R> object(Name aName) {
        return object(aName, null);
    }

    @Override
    public <R> NamedObject<R> object(String aName, Class<R> aType) {
        return object(alias(aName, aName), aType);
    }

    @Override
    public <R> NamedObject<R> object(final Name aName, final Class<R> aType) {
        return new NamedObject<R>(){
            private List<Content<?>> content;
            private Class<?> intermediate;
            private Converter<?,R> converter;

            @Override
            public <I> NamedObject<I> via(Class<I> anIntermediateType) {
                getConverter(anIntermediateType, aType);
                intermediate = anIntermediateType;
                return (NamedObject<I>) this;
            }

            @Override
            public <I> NamedObject<I> via(Class<I> anIntermediateType, Converter<I, R> aConverter) {
                intermediate = anIntermediateType;
                converter = aConverter;
                return (NamedObject<I>) this;
            }

            @Override
            public NamedObject<R> with(Content<?>... aContent) {
                content = Arrays.asList(aContent);
                return this;
            }

            @Override
            public Name getName() {
                return aName;
            }
        };
    }

    @Override
    public <R> UnNamedObject<R> object(final Class<R> aType) {
        return new UnNamedObject<R>(){
            private List<Content<?>> content;
            private Converter<?,R> converter;
            private Class<?> intermediate;

            @Override
            public <I> UnNamedObject<I> via(Class<I> anIntermediateType) {
                intermediate = anIntermediateType;
                converter = getConverter(intermediate, aType);
                return (UnNamedObject<I>)this;
            }

            @Override
            public <I> UnNamedObject<I> via(Class<I> anIntermediateType, Converter<I, R> aConverter) {
                intermediate = anIntermediateType;
                converter = aConverter;
                return (UnNamedObject<I>)this;
            }

            @Override
            public UnNamedObject<R> with(Content<?>... aContent) {
                content = Arrays.asList(aContent);
                return this;
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }
        };
    }

    @Override
    public <R> NamedArray<R> array(String aName) {
        return array(alias(aName, aName), null);
    }

    @Override
    public <R> NamedArray<R> array(Name aName) {
        return array(aName, null);
    }

    @Override
    public <R> NamedArray<R> array(String aName, Converter<List, R> aConverter) {
        return array(alias(aName, aName), aConverter);
    }

    @Override
    public <R> NamedArray<R> array(Name aName, Converter<List, R> aConverter) {
        return new NamedArray(){
            @Override
            public NamedArray of(Class<?> aConvertableType) {
                return null;  // TODO
            }

            @Override
            public NamedArray of(UnNamedProperty<?> aContent) {
                return null;  // TODO
            }

            @Override
            public NamedArray of(UnNamedObject<?> aContent) {
                return null;  // TODO
            }

            @Override
            public NamedArray of(UnNamedArray<?> aContent) {
                return null;  // TODO
            }

            @Override
            public Name getName() {
                return null;  // TODO
            }
        };
    }

    @Override
    public <R> UnNamedArray<R> array() {
        return array(new Converter<List,R>(){
            @Override
            public R convert(List aFrom) {
                return (R)aFrom;
            }
        });
    }

    @Override
    public <R> UnNamedArray<R> array(Converter<List, R> aConverter) {
        return new UnNamedArray<R>(){
            @Override
            public UnNamedArray<R> of(Class<?> aConvertableType) {
                return null;  // TODO
            }

            @Override
            public UnNamedArray<R> of(UnNamedProperty<?> aContent) {
                return null;  // TODO
            }

            @Override
            public UnNamedArray<R> of(UnNamedObject<?> aContent) {
                return null;  // TODO
            }

            @Override
            public UnNamedArray<R> of(UnNamedArray<?> aContent) {
                return null;  // TODO
            }

            @Override
            public Name getName() {
                return null;  // TODO
            }
        };
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

    public <T> StringConverter<T> getConverter(Class<T> aTo) {
        return (StringConverter<T>) getConverter(String.class, aTo);
    }

    public <F,T> TypeSafeConverter<F,T> getConverter(Class<F> aFromType, Class<T> aToType) {
        for (TypeSafeConverter<?,?> _c : converters) {
            if ((_c.canConvertFrom(aFromType)) && (_c.canConvertTo(aToType))) {
                return (TypeSafeConverter<F,T>) _c;
            }
        }

        throw new RuntimeException("no converter registered that can convert from " + aFromType + " to " + aToType);
    }
}
