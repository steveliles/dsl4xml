package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.ReflectiveBuilder;
import com.sjl.dsl4xml.support.ReflectorFactory;

import java.util.ArrayList;
import java.util.Collections;

public class Array<R> {

    private Converter<?,? extends R> converter;
    private UnNamedObject<?> obj;
    private UnNamedProperty<?,?> property;
    private UnNamedArray<?> array;
    private Class<?> intermediate;
    private ReflectorFactory reflector = new ReflectorFactory();

    private Class<? extends R> type;
    private Name name;

    public Array(Name aName, Class<? extends R> aType) {
        name = aName;
        type = aType;
    }

    public void of(UnNamedProperty<?,?> aContent) {
        property = aContent;
    }

    public void of(UnNamedObject<?> aContent) {
        obj = aContent;
    }

    public void of(UnNamedArray<?> aContent) {
        array = aContent;
    }

    public void onAttach(Class<?> aContainerType, ReflectorFactory aReflector, ConverterRegistry aConverters) {
        Content<?> _def = JsonDocumentDefinition.firstNonNull(obj, property, array);

        if (type.isInterface())
        {
            intermediate = ArrayList.class;
            converter = aConverters.getConverter(ArrayList.class, type);
            aReflector.prepare(aContainerType, name, intermediate);
            _def.onAttach(intermediate, reflector, aConverters);
        }
        else
        {
            Class<?> _type = ReflectorFactory.maybeConvertToProxy(type);
            aReflector.prepare(aContainerType, name, _type);
            _def.onAttach(_type, reflector, aConverters);
        }
    }

    public Name getName() {
        return name;
    }

    public Builder<R> newBuilder() {
        Definition<?> _def = JsonDocumentDefinition.firstNonNull(obj, property, array);
        return new ReflectiveBuilder(
                name, type, intermediate, converter,
                reflector.newReflector(), Collections.singletonList(_def.newBuilder()), true);
    }
}
