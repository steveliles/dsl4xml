package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Context;
import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.Name;
import com.sjl.dsl4xml.ParsingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO - extract to dsl4 support
public class ReflectiveBuilder<T> implements Builder<T> {

    private Name name;
    private Class<T> target;
    private Class<?> intermediate;
    private Converter<Object,T> converter;
    private Reflector reflector;
    private List<Builder<?>> nested;
    private Map<String,String> aliases;
    private boolean array;

    public ReflectiveBuilder(
            Name aName, Class<T> aTarget, Class<?> anIntermediate, Converter<Object,T> aConverter,
            Reflector aReflector, List<Builder<?>> aNested, boolean anIsArray) {
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
        array = anIsArray;

        if (nested != null)
        {
            aliases = new HashMap<String,String>();
            for (Builder<?> _b : nested)
                aliases.put(_b.getName().getName(), _b.getName().getAlias());
        }
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public Class<T> getTargetType() {
        return target;
    }

    @Override
    public Builder<?> moveDown(String aName) {
        for (Builder<?> _b : nested) {
            if ((aName.equals(_b.getName().getName())) ||
                (_b.getName().equals(Name.MISSING) && nested.size()==1)) {
                return _b;
            }
        }
        return null;
    }

    @Override
    public void prepare(Context aContext) {
        aContext.push(reflector.newInstance(intermediate));
    }

    @Override
    public void setValue(Context aContext, String aName, Object aValue) {
        String _propertyName = aliases.get(aName);
        if ((_propertyName == null) || ("".equals(_propertyName))) _propertyName = aName;
        Method _m = reflector.getMutator(intermediate, _propertyName, aValue);
        if (_m == null)
            throw new IllegalStateException(
                "No mutator method found for '" + _propertyName + "' of type " +
                aValue.getClass().getName() + " in target " + intermediate);

//        if (!_m.getDeclaringClass().equals(intermediate))
//        {
//            throw new IllegalArgumentException(
//                "cannot invoke " + _m.getName() + " on " + intermediate.getName() + " - it is not the declaring class!");
//        }

        try {
            T _ctx = aContext.peek();
            if (intermediate.isAssignableFrom(_ctx.getClass())) {
                switch (_m.getParameterTypes().length) {
                    case 1 :
                        _m.invoke(aContext.peek(), aValue);
                        break;
                    case 2 :
                        _m.invoke(aContext.peek(), _propertyName, aValue);
                        break;
                    default:
                        throw new NoSuitableMethodException(
                            "Don't know how to deal with mutator method " + _m.getName() +
                            " of " + aContext.getClass().getName() + " which requires " +
                            _m.getParameterTypes().length + " parameters");
                }
            } else {
                throw new ParsingException("Expected " + _propertyName + " to be a " + intermediate.getName() + " but got a " + _ctx.getClass().getName());
            }
        } catch (IllegalAccessException anExc) {
            throw new ParsingException("Not allowed to invoke " + _m.getName() + " on " + intermediate.getName(), anExc);
        } catch (InvocationTargetException anExc) {
            throw new ParsingException("Problem while invoking " + _m.getName() + " on " + intermediate.getName(), anExc);
        }
    }

    @Override
    public T build(Context aContext) {
        if (converter != null)
            return converter.convert(aContext.pop());
        else {
            return aContext.pop();
        }
    }
}
