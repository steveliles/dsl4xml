package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.TypeSafeConverter;
import com.sjl.dsl4xml.support.*;
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
            throw new IllegalStateException(
                "document already defined - too late to register general converters. Either register converters " +
                "before .mapping(class) or use .via(class, converter) to nominate a converter per object mapping.");

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
            private Converter<Object,T> converter;
            private Reflector reflector = new ThreadSafeCachingReflector();

            @Override
            public Document<T> with(Content<?>... aContent) {
                content = Arrays.asList(aContent);
                return this;
            }

            @Override
            public <I> Document<I> via(Class<I> anIntermediateType) {
                intermediate = anIntermediateType;
                converter = (Converter<Object,T>)getConverter(anIntermediateType, aType);
                return (Document<I>)this;
            }

            @Override
            public <I> Document<I> via(Class<I> anIntermediateType, Converter<I,? extends T> aConverter) {
                intermediate = anIntermediateType;
                converter = (Converter<Object,T>)aConverter;
                return (Document<I>)this;
            }

            @Override
            @SuppressWarnings("rawtypes")
            public Builder<T> newBuilder() {
                if (intermediate != null) {
                    return new ReflectiveBuilder(aType, reflector);
                } else {
                    return new ReflectiveBuilderWithIntermediate(aType, intermediate, converter, reflector);
                }
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
    public <R> NamedObject<R> object(String aName, Class<? extends R> aType) {
        return object(alias(aName, aName), aType);
    }

    @Override
    public <R> NamedObject<R> object(final Name aName, final Class<? extends R> aType) {
        return new NamedObject<R>(){
            private List<Content<?>> content;
            private Class<?> intermediate;
            private Converter<?,R> converter;

            @Override
            public Class<? extends R> getTargetType() {
                return aType;
            }

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)intermediate;
            }

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
    public <R> UnNamedObject<R> object(final Class<? extends R> aType) {
        return new UnNamedObject<R>(){
            private List<Content<?>> content;
            private Converter<?,R> converter;
            private Class<?> intermediate;

            @Override
            public Class<? extends R> getTargetType() {
                return aType;
            }

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)intermediate;
            }

            @Override
            public <I> UnNamedObject<I> via(Class<I> anIntermediateType) {
                intermediate = anIntermediateType;
                converter = (Converter<?,R>) getConverter(intermediate, aType);
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
    public <R> NamedArray<R> array(String aName, Converter<List, ? extends R> aConverter) {
        return array(alias(aName, aName), aConverter);
    }

    @Override
    public <R> NamedArray<R> array(final Name aName, final Converter<List, ? extends R> aConverter) {
        return new NamedArray<R>(){
            private UnNamedObject<?> obj;
            private UnNamedProperty<?> property;
            private UnNamedArray<?> array;
            private Converter<List,? extends R> converter;

            @Override
            public Class<?> getIntermediateType() {
                return List.class;
            }

            @Override
            public Class<R> getTargetType() {
                return null;
            }

            @Override
            public NamedArray<R> of(Class<?> aConvertableType) {
                obj = object(aConvertableType);
                return this;
            }

            @Override
            public NamedArray<R> of(UnNamedProperty<?> aContent) {
                property = aContent;
                return this;
            }

            @Override
            public NamedArray<R> of(UnNamedObject<?> aContent) {
                obj = aContent;
                return this;
            }

            @Override
            public NamedArray<R> of(UnNamedArray<?> aContent) {
                array = aContent;
                return this;
            }

            @Override
            public Name getName() {
                return aName;
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
    public <R> UnNamedArray<R> array(Converter<List, ? extends R> aConverter) {
        return new UnNamedArray<R>(){
            private UnNamedObject<?> obj;
            private UnNamedProperty<?> property;
            private UnNamedArray<?> array;
            private Converter<List,? extends R> converter;

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)List.class;
            }

            @Override
            public Class<? extends R> getTargetType() {
                return null;
            }

            @Override
            public UnNamedArray<R> of(Class<?> aConvertableType) {
                obj = object(aConvertableType);
                return this;
            }

            @Override
            public UnNamedArray<R> of(UnNamedProperty<?> aContent) {
                property = aContent;
                return this;
            }

            @Override
            public UnNamedArray<R> of(UnNamedObject<?> aContent) {
                obj = aContent;
                return this;
            }

            @Override
            public UnNamedArray<R> of(UnNamedArray<?> aContent) {
                array = aContent;
                return this;
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }
        };
    }

    @Override
    public <R> UnNamedProperty<R> property(final Class<? extends R> aClass) {
        return new UnNamedProperty<R>(){
            private StringConverter<? extends R> converter = getConverter(aClass);

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)String.class;
            }

            @Override
            public Class<? extends R> getTargetType() {
                return aClass;
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }
        };
    }

    @Override
    public <R> NamedProperty<R> property(String aName) {
        return property(alias(aName, aName));
    }

    @Override
    public <R> NamedProperty<R> property(final Name aName) {
        return new NamedProperty<R>(){
            @Override
            public Class<? extends R> getTargetType() {
                return null;
            }

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)String.class;
            }

            @Override
            public Name getName() {
                return aName;  // TODO
            }
        };
    }

    @Override
    public <R> NamedProperty<R> number(String aName, Class<R> aType) {
        return property(alias(aName, aName));
    }

    @Override
    public <R> NamedProperty<R> number(final Name aName, final Class<R> aType) {
        return new NamedProperty<R>(){
            private Class<?> intermediate;

            @Override
            public Class<? extends R> getTargetType() {
                return aType;
            }

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)intermediate;
            }

            @Override
            public Name getName() {
                return aName;
            }
        };
    }

    @Override
    public <R> UnNamedProperty<R> number(final Class<R> aType) {
        return new UnNamedProperty<R>(){
            private Class<?> intermediate;

            @Override
            public Class<? extends R> getTargetType() {
                return aType;
            }

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)intermediate;
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }
        };
    }

    @Override
    public <R> NamedProperty<R> bool(String aName) {
        return bool(alias(aName, aName));
    }

    @Override
    public <R> NamedProperty<R> bool(final Name aName) {
        return (NamedProperty<R>)bool(aName, Boolean.class);
    }

    @Override
    public <R> NamedProperty<R> bool(String aName, Class<R> aType) {
        return bool(alias(aName, aName), aType);
    }

    @Override
    public <R> NamedProperty<R> bool(final Name aName, final Class<R> aType) {
        return new NamedProperty<R>(){
            @Override
            public Class<? extends R> getTargetType() {
                return aType;
            }

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)Boolean.class;
            }

            @Override
            public Name getName() {
                return aName;
            }
        };
    }

    @Override
    public <R> UnNamedProperty<R> bool() {
        return (UnNamedProperty<R>)bool(Boolean.class);
    }

    @Override
    public <R> UnNamedProperty<R> bool(final Class<R> aType) {
        return new UnNamedProperty<R>() {
            @Override
            public Class<R> getTargetType() {
                return aType;
            }

            @Override
            public <I> Class<I> getIntermediateType() {
                return (Class<I>)Boolean.class;
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }
        };
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
                converters.add(new TypeSafeConverter<F,Object>(){
                    @Override
                    public boolean canConvertFrom(Class<?> aClass) {
                        return aFrom.isAssignableFrom(aClass);
                    }

                    @Override
                    public boolean canConvertTo(Class<?> aClass) {
                        return to.isAssignableFrom(aClass);
                    }

                    @Override
                    public Object convert(F aFrom) {
                        return converter.convert(aFrom);
                    }
                });
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
