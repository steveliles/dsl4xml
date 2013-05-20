package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.ParsingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveBuilder<T> implements Builder<T> {

    private T prepared;

    private Reflector reflector;
    private Class<T> target;

    public ReflectiveBuilder(Class<T> aTarget, Reflector aReflector) {
        if (aTarget == null)
            throw new IllegalArgumentException("Must supply a target type");
        if (aReflector == null)
            throw new IllegalArgumentException("Must supply a reflector");

        target = aTarget;
        reflector = aReflector;
    }

    @Override
    public void prepare() {
        prepared = Classes.newInstance(target);
    }

    @Override
    public void setValue(String aName, Object aValue) {
        Method _m = reflector.getMutator(target, aName, aValue);
        try {
            _m.invoke(prepared, aValue);
        } catch (IllegalAccessException anExc) {
            throw new ParsingException("Not allowed to invoke " + _m.getName() + " on " + target.getName(), anExc);
        } catch (InvocationTargetException anExc) {
            throw new ParsingException("Problem while invoking " + _m.getName() + " on " + target.getName(), anExc);
        }

    }

    @Override
    public T build() {
        return prepared;
    }
}
