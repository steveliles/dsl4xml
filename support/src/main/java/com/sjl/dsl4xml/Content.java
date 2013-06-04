package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.ReflectorFactory;

public interface Content<T> extends Definition<T> {

    public Name getName();

    public void onAttach(Class<?> aContainerType, ReflectorFactory aFactory, ConverterRegistry aConverters);

}
