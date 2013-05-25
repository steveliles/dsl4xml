package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.ParsingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

// TODO - extract to dsl4 support
public class ReflectiveBuilderWithIntermediate<I,T> implements Builder<T> {

    private Name name;
    private Class<T> target;
    private Class<I> intermediate;
    private Converter<I,T> converter;
    private Reflector reflector;
    private List<Builder<?>> nested;

    public ReflectiveBuilderWithIntermediate(
        Name aName, Class<T> aTarget, Class<I> anIntermediate, Converter<I, T> aConverter,
        Reflector aReflector, List<Builder<?>> aNested) {
        if (aName == null)
            throw new IllegalArgumentException("Must supply a name");
        if (aTarget == null)
            throw new IllegalArgumentException("Must supply a target type");
        if (anIntermediate == null)
            throw new IllegalArgumentException("Must supply an intermediate type");
        if (aConverter == null)
            throw new IllegalArgumentException("Must supply a converter");
        if (aReflector == null)
            throw new IllegalArgumentException("Must supply a reflector");

        name = aName;
        target = aTarget;
        intermediate = anIntermediate;
        converter = aConverter;
        reflector = aReflector;
        nested = aNested;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Class<T> getTargetType() {
        return target;
    }

    @Override
    public Builder<?> moveDown(Context aContext) {
        return aContext.select(nested);
    }

    @Override
    public void prepare(Context aContext) {
        aContext.push(reflector.newInstance(intermediate));
    }

    @Override
    public void setValue(Context aContext, String aName, Object aValue) {
        Method _m = reflector.getMutator(intermediate, aName, aValue);
        try {
            I _ctx = aContext.peek();
            if (intermediate.isAssignableFrom(_ctx.getClass())) {
                _m.invoke(aContext.peek(), aValue);
            } else {
                throw new ParsingException("Expected " + aName + " to be a " + intermediate.getName() + " but got a " + _ctx.getClass().getName());
            }
        } catch (IllegalAccessException anExc) {
            throw new ParsingException("Not allowed to invoke " + _m.getName() + " on " + intermediate.getName(), anExc);
        } catch (InvocationTargetException anExc) {
            throw new ParsingException("Problem while invoking " + _m.getName() + " on " + intermediate.getName(), anExc);
        }
    }

    @Override
    public T build(Context aContext) {
        return converter.convert((I)aContext.pop());
    }
}
