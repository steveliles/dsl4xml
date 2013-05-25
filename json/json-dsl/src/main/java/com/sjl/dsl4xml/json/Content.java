package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.support.HasConverters;
import com.sjl.dsl4xml.support.Name;

public interface Content<T> extends Definition<T> {

    public Name getName();

    public void onAttach(Class<?> aContainerType, HasConverters aConverters);

}
