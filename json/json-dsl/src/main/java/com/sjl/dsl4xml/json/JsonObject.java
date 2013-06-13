package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Content;
import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.ConverterRegistry;
import com.sjl.dsl4xml.Name;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.ReflectiveBuilder;
import com.sjl.dsl4xml.support.ReflectorFactory;

import java.util.ArrayList;
import java.util.List;

public class JsonObject<R>  {
    private Name name;
    private Class<? extends R> type;
    private List<Content<?>> content;
    private Class<?> intermediate;
    private Converter<?,R> converter;
    private ReflectorFactory reflector = new ReflectorFactory();

    public JsonObject(Name aName, Class<? extends R> aType) {
        name = aName;
        type = aType;
    }

    public <I> void via(Class<I> anIntermediateType) {
        intermediate = anIntermediateType;
    }

    public <I> void via(Class<I> anIntermediateType, Converter<I, R> aConverter) {
        intermediate = anIntermediateType;
        converter = aConverter;
    }

    public void with(Content<?>... aContent) {
        content = new ArrayList<Content<?>>();
        for (Content<?> _c : aContent) {
            content.add(_c);
        }
    }

    public void onAttach(Class<?> aContainerType, ReflectorFactory aReflector, ConverterRegistry aConverters) {
        if ((intermediate != null) && (converter == null))
            converter = (Converter<?,R>)aConverters.getConverter(intermediate, type);
        Class<?> _attachTo = ReflectorFactory.maybeConvertToProxy((intermediate != null) ? intermediate : type);
        for (Content _c : content)
            _c.onAttach(_attachTo, reflector, aConverters);
        aReflector.prepare(aContainerType, name, _attachTo);
    }

    public Name getName() {
        return name;
    }

    public Builder<R> newBuilder() {
        List<Builder<?>> _nested = new ArrayList<Builder<?>>();
        for (Content<?> _c : content)
            _nested.add(_c.newBuilder());

        return new ReflectiveBuilder(
            name, type, intermediate, converter,
            reflector.newReflector(), _nested, false);
    }
}
