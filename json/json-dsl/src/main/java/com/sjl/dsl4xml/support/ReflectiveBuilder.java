package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.ParsingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveBuilder<T> implements Builder<T> {

    private Name name;
    private Class<T> target;
    private Reflector reflector;
    private List<Builder<?>> nested;

    public ReflectiveBuilder(Name aName, Class<T> aTarget, Reflector aReflector, List<Builder<?>> aNested) {
        if (aName == null)
            throw new IllegalArgumentException("Must suppky a name");
        if (aTarget == null)
            throw new IllegalArgumentException("Must supply a target type");
        if (aReflector == null)
            throw new IllegalArgumentException("Must supply a reflector");

        name = aName;
        target = aTarget;
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
        aContext.push(reflector.newInstance(target));
    }

    @Override
    public void setValue(Context aContext, String aName, Object aValue) {
        Method _m = reflector.getMutator(target, aName, aValue);
        try {
            T _ctx = aContext.peek();
            if (target.isAssignableFrom(_ctx.getClass())) {
                _m.invoke(aContext.peek(), aValue);
            } else {
                throw new ParsingException("Expected " + aName + " to be a " + target.getName() + " but got a " + _ctx.getClass().getName());
            }
        } catch (IllegalAccessException anExc) {
            throw new ParsingException("Not allowed to invoke " + _m.getName() + " on " + target.getName(), anExc);
        } catch (InvocationTargetException anExc) {
            throw new ParsingException("Problem while invoking " + _m.getName() + " on " + target.getName(), anExc);
        }
    }

    @Override
    public T build(Context aContext) {
        return aContext.pop();
    }
}
