package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.ParsingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// TODO - extract to dsl4 support
public class ReflectiveBuilderWithIntermediate<I,T> implements Builder<T> {
    private I prepared;

    private Reflector reflector;
    private Class<T> target;
    private Class<I> intermediate;
    private Converter<I,T> converter;

    public ReflectiveBuilderWithIntermediate(
        Class<T> aTarget, Class<I> anIntermediate,
        Converter<I, T> aConverter, Reflector aReflector) {
        if (aTarget == null)
            throw new IllegalArgumentException("Must supply a target type");
        if (anIntermediate == null)
            throw new IllegalArgumentException("Must supply an intermediate type");
        if (aConverter == null)
            throw new IllegalArgumentException("Must supply a converter");
        if (aReflector == null)
            throw new IllegalArgumentException("Must supply a reflector");

        target = aTarget;
        intermediate = anIntermediate;
        converter = aConverter;
        reflector = aReflector;
    }

    @Override
    public void prepare() {
        prepared = Classes.newInstance(intermediate);
    }

    @Override
    public void setValue(String aName, Object aValue) {
        Method _m = reflector.getMutator(intermediate, aName, aValue);
        try {
            _m.invoke(prepared, aValue);
        } catch (IllegalAccessException anExc) {
            throw new ParsingException("Not allowed to invoke " + _m.getName() + " on " + intermediate.getName(), anExc);
        } catch (InvocationTargetException anExc) {
            throw new ParsingException("Problem while invoking " + _m.getName() + " on " + intermediate.getName(), anExc);
        }
    }

    @Override
    public T build() {
        return converter.convert(prepared);
    }
}
