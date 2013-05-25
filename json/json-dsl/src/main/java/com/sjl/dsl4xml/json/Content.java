package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.support.HasConverters;
import com.sjl.dsl4xml.support.Name;
import com.sjl.dsl4xml.support.ReflectorFactory;

public interface Content<T> extends Definition<T> {

    public Name getName();

    public void onAttach(Class<?> aContainerType, ReflectorFactory aFactory, HasConverters aConverters);

}
