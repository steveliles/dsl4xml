package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.ParsingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

// TODO - extract to dsl4 support
public class ReflectiveBuilder<T> implements Builder<T> {

    private Name name;
    private Class<T> target;
    private Class<?> intermediate;
    private Converter<Object,T> converter;
    private Reflector reflector;
    private List<Builder<?>> nested;

    public ReflectiveBuilder(
            Name aName, Class<T> aTarget, Class<?> anIntermediate, Converter<Object,T> aConverter,
            Reflector aReflector, List<Builder<?>> aNested) {
        if (aName == null)
            throw new IllegalArgumentException("Must supply a name");
        if (aTarget == null)
            throw new IllegalArgumentException("Must supply a target type");
        if (aReflector == null)
            throw new IllegalArgumentException("Must supply a reflector");

        name = aName;
        target = aTarget;
        intermediate = (anIntermediate == null) ? aTarget : anIntermediate;
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
        Method _m = reflector.getMutator(target, aName, aValue);
        if (_m == null)
            throw new IllegalStateException(
                "No mutator method found for '" + aName + "' of type " +
                aValue.getClass().getName() + " in target " + aContext);

        try {
            T _ctx = aContext.peek();
            if (target.isAssignableFrom(_ctx.getClass())) {
                switch (_m.getParameterTypes().length) {
                    case 1 :
                        _m.invoke(aContext.peek(), aValue);
                        break;
                    case 2 :
                        _m.invoke(aContext.peek(), aName, aValue);
                        break;
                    default:
                        throw new NoSuitableMethodException(
                            "Don't know how to deal with mutator method " + _m.getName() +
                            " of " + aContext.getClass().getName() + " which requires " +
                            _m.getParameterTypes().length + " parameters");
                }
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
        if (converter != null)
            return converter.convert(aContext.pop());
        else
            return aContext.pop();
    }
}
