package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.TypeSafeConverter;
import com.sjl.dsl4xml.support.*;
import com.sjl.dsl4xml.support.convert.*;

import java.util.*;

public class JsonDocumentDefinition<T> implements DocumentDefinition<T>, HasConverters {

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
            public String getNamespace() {
                return "";
            }

            @Override
            public String getName() {
                return aName;
            }

            @Override
            public String getAlias() {
                return anAlias;
            }

            @Override
            public String toString() {
                return "'" + aName + "(" + anAlias + ")'";
            }

            @Override
            public int hashCode() {
                return getClass().hashCode() ^ aName.hashCode() ^ anAlias.hashCode();
            }

            @Override
            public boolean equals(Object anObject) {
                if (anObject instanceof Name) {
                    Name _other = (Name) anObject;
                    return
                        ((aName != null) && (aName.equals(_other.getName()))) &&
                        ((anAlias != null) && (anAlias.equals(_other.getAlias())));

                }
                return false;
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
            private ReflectorFactory reflector = new ReflectorFactory();

            @Override
            public Document<T> with(Content<?>... aContent) {
                content = new ArrayList<Content<?>>();
                for (Content<?> _c : aContent) {
                    _c.onAttach(aType, JsonDocumentDefinition.this);
                    content.add(_c);
                }
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
                List<Builder<?>> _nested = new ArrayList<Builder<?>>();
                for (Content<?> _c : content)
                    _nested.add(_c.newBuilder());
                if (intermediate != null) {
                    return new ReflectiveBuilder(Name.MISSING, aType, reflector.newReflector(), _nested);
                } else {
                    return new ReflectiveBuilderWithIntermediate(
                        Name.MISSING, aType, intermediate, converter, reflector.newReflector(), _nested);
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
            private ReflectorFactory reflector = new ReflectorFactory();

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
                content = new ArrayList<Content<?>>();
                for (Content<?> _c : aContent) {
                    _c.onAttach(aType, JsonDocumentDefinition.this);
                    content.add(_c);
                }
                return this;
            }

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (reflector.hasMutator(aContainerType, aName, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for object named " + aName +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return aName;
            }

            // TODO: this is identical in Document
            @Override
            @SuppressWarnings("rawtypes")
            public Builder<T> newBuilder() {
                List<Builder<?>> _nested = new ArrayList<Builder<?>>();
                for (Content<?> _c : content)
                    _nested.add(_c.newBuilder());
                if (intermediate != null) {
                    return new ReflectiveBuilder(aName, aType, reflector.newReflector(), _nested);
                } else {
                    return new ReflectiveBuilderWithIntermediate(
                        aName, aType, intermediate, converter, reflector.newReflector(), _nested);
                }
            }
        };
    }

    @Override
    public <R> UnNamedObject<R> object(final Class<? extends R> aType) {
        return new UnNamedObject<R>(){
            private List<Content<?>> content;
            private Converter<?,R> converter;
            private Class<?> intermediate;
            private ReflectorFactory reflector = new ReflectorFactory();

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
                content = new ArrayList<Content<?>>();
                for (Content<?> _c : aContent) {
                    _c.onAttach(aType, JsonDocumentDefinition.this);
                    content.add(_c);
                }
                return this;
            }

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (reflector.hasMutator(aContainerType, Name.MISSING, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for unnamed property " +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }

            // TODO: this is identical in Document and elsewhere
            @Override
            @SuppressWarnings("rawtypes")
            public Builder<T> newBuilder() {
                List<Builder<?>> _nested = new ArrayList<Builder<?>>();
                for (Content<?> _c : content)
                    _nested.add(_c.newBuilder());
                if (intermediate != null) {
                    return new ReflectiveBuilder(
                        Name.MISSING, aType, reflector.newReflector(), _nested);
                } else {
                    return new ReflectiveBuilderWithIntermediate(
                        Name.MISSING, aType, intermediate, converter, reflector.newReflector(), _nested);
                }
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
    public <R> NamedArray<R> array(String aName, Class<? extends R> aType) {
        return array(alias(aName, aName));
    }

    @Override
    public <R> NamedArray<R> array(final Name aName, final Class<? extends R> aType) {
        return new NamedArray<R>(){
            private UnNamedObject<?> obj;
            private UnNamedProperty<?,?> property;
            private UnNamedArray<?> array;
            private Class<?> intermediate;
            private Converter<?,? extends R> converter;
            private ReflectorFactory reflector = new ReflectorFactory();

            @Override
            public <I> NamedArray<I> via(Class<I> anIntermediateType) {
                intermediate = anIntermediateType;
                converter = (Converter<?,R>) getConverter(intermediate, aType);
                return (NamedArray<I>)this;
            }

            @Override
            public <I> NamedArray<I> via(Class<I> anIntermediateType, Converter<I, R> aConverter) {
                intermediate = anIntermediateType;
                converter = aConverter;
                return (NamedArray<I>)this;
            }

            @Override
            public NamedArray<R> of(Class<?> aConvertableType) {
                obj = object(aConvertableType);
                obj.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public NamedArray<R> of(UnNamedProperty<?,?> aContent) {
                property = aContent;
                property.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public NamedArray<R> of(UnNamedObject<?> aContent) {
                obj = aContent;
                obj.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public NamedArray<R> of(UnNamedArray<?> aContent) {
                array = aContent;
                array.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (reflector.hasMutator(aContainerType, aName, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for array named " + aName +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return aName;
            }

            @Override
            @SuppressWarnings("rawtypes")
            public Builder<T> newBuilder() {
                Definition<?> _def = firstNonNull(obj, property, array);
                if (intermediate != null) {
                    return new ReflectiveBuilder(
                        aName, aType, reflector.newReflector(), Collections.singletonList(_def));
                } else {
                    return new ReflectiveBuilderWithIntermediate(
                        aName, aType, intermediate, converter,
                        reflector.newReflector(), Collections.singletonList(_def));
                }
            }
        };
    }

    @Override
    public UnNamedArray<List> array() {
        return array(List.class);
    }

    @Override
    public <R> UnNamedArray<R> array(final Class<? extends R> aType) {
        return new UnNamedArray<R>(){
            private UnNamedObject<?> obj;
            private UnNamedProperty<?,?> property;
            private UnNamedArray<?> array;
            private Class<?> intermediate;
            private Converter<?,? extends R> converter;
            private ReflectorFactory reflector = new ReflectorFactory();

            @Override
            public <I> UnNamedArray<I> via(Class<I> anIntermediateType) {
                intermediate = anIntermediateType;
                converter = (Converter<?,R>) getConverter(intermediate, aType);
                return (UnNamedArray<I>)this;
            }

            @Override
            public <I> UnNamedArray<I> via(Class<I> anIntermediateType, Converter<I, R> aConverter) {
                intermediate = anIntermediateType;
                converter = aConverter;
                return (UnNamedArray<I>)this;
            }

            @Override
            public UnNamedArray<R> of(Class<?> aConvertableType) {
                obj = object(aConvertableType);
                obj.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public UnNamedArray<R> of(UnNamedProperty<?,?> aContent) {
                property = aContent;
                property.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public UnNamedArray<R> of(UnNamedObject<?> aContent) {
                obj = aContent;
                obj.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public UnNamedArray<R> of(UnNamedArray<?> aContent) {
                array = aContent;
                array.onAttach(List.class, JsonDocumentDefinition.this);
                return this;
            }

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (reflector.hasMutator(aContainerType, Name.MISSING, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for unnamed array " +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }

            @Override
            @SuppressWarnings("rawtypes")
            public Builder<T> newBuilder() {
                Definition<?> _def = firstNonNull(obj, property, array);
                if (intermediate != null) {
                    return new ReflectiveBuilder(
                        Name.MISSING, aType, reflector.newReflector(), Collections.singletonList(_def));
                } else {
                    return new ReflectiveBuilderWithIntermediate(
                        Name.MISSING, aType, intermediate, converter,
                        reflector.newReflector(), Collections.singletonList(_def));
                }
            }
        };
    }

    @Override
    public <R> UnNamedProperty<String,R> property(final Class<? extends R> aType) {
        return new UnNamedProperty<String,R>(){
            private StringConverter<? extends R> converter = getConverter(aType);

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (new ReflectorFactory().hasMutator(aContainerType, Name.MISSING, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for unnamed property " +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }

            @Override
            public Builder<R> newBuilder() {
                return new PropertyBuilder<String,R>(Name.MISSING, aType, converter);
            }
        };
    }

    @Override
    public <R> NamedProperty<String,R> property(String aName) {
        return property(alias(aName, aName));
    }

    @Override
    public <R> NamedProperty<String,R> property(final Name aName) {
        return new NamedProperty<String,R>(){
            private Class<? extends R> type;
            private StringConverter<? extends R> converter;

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                type = (Class<? extends R>)new ReflectorFactory().getExpectedType(aContainerType, aName);
                converter = getConverter(type);
            }

            @Override
            public Name getName() {
                return aName;  // TODO
            }

            @Override
            public Builder<R> newBuilder() {
                return new PropertyBuilder<String,R>(aName, type, converter);
            }
        };
    }

    @Override
    public <R> NamedProperty<Number,R> number(String aName, Class<R> aType) {
        return number(alias(aName, aName), aType);
    }

    @Override
    public <R> NamedProperty<Number,R> number(final Name aName, final Class<R> aType) {
        return new NamedProperty<Number,R>(){
            private Converter<Number,R> converter = getConverter(Number.class, aType);

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                Reflector _reflector = new ReflectorFactory();
                if (_reflector.hasMutator(aContainerType, aName, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for number property " + aName +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return aName;
            }

            @Override
            public Builder<R> newBuilder() {
                return new PropertyBuilder<Number,R>(aName, aType, converter);
            }
        };
    }

    @Override
    public <R> UnNamedProperty<Number,R> number(final Class<R> aType) {
        return new UnNamedProperty<Number,R>(){
            private Converter<Number,R> converter = getConverter(Number.class, aType);

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (new ReflectorFactory().hasMutator(aContainerType, Name.MISSING, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for unnamed number property " +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }

            @Override
            public Builder<R> newBuilder() {
                return new PropertyBuilder<Number,R>(Name.MISSING, aType, converter);
            }
        };
    }

    @Override
    public <R> NamedProperty<Boolean,R> bool(String aName) {
        return bool(alias(aName, aName));
    }

    @Override
    public <R> NamedProperty<Boolean,R> bool(final Name aName) {
        throw new UnsupportedOperationException("TODO"); // TODO: figure out R from the context
        //return bool(aName, Boolean.class);
    }

    @Override
    public <R> NamedProperty<Boolean,R> bool(String aName, Class<R> aType) {
        return bool(alias(aName, aName), aType);
    }

    @Override
    public <R> NamedProperty<Boolean,R> bool(final Name aName, final Class<R> aType) {
        return new NamedProperty<Boolean,R>(){
            private Converter<Boolean,R> converter = getConverter(Boolean.class, aType);

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (new ReflectorFactory().hasMutator(aContainerType, aName, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for boolean property " + aName +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return aName;
            }

            @Override
            public Builder<R> newBuilder() {
                return new PropertyBuilder<Boolean,R>(aName, aType, converter);
            }
        };
    }

    @Override
    public <R> UnNamedProperty<Boolean,R> bool() {
        throw new UnsupportedOperationException("TODO"); // TODO: figure out R from the context
        //return bool(Boolean.class);
    }

    @Override
    public <R> UnNamedProperty<Boolean,R> bool(final Class<R> aType) {
        return new UnNamedProperty<Boolean,R>() {
            private Converter<Boolean,R> converter = getConverter(Boolean.class, aType);

            @Override
            public void onAttach(Class<?> aContainerType, HasConverters aConverters) {
                if (new ReflectorFactory().hasMutator(aContainerType, Name.MISSING, aType))
                    throw new NoSuitableMethodException(
                        "Can't find a mutator method for unnamed boolean property " +
                        " of type " + aType.getName() +
                        " on target class " + aContainerType);
            }

            @Override
            public Name getName() {
                return Name.MISSING;
            }

            @Override
            public Builder<R> newBuilder() {
                return new PropertyBuilder<Boolean,R>(Name.MISSING, aType, converter);
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

    private <T> T firstNonNull(T... aTs) {
        for (T _t : aTs)
            if (_t != null)
                return _t;
        return null;
    }
}
