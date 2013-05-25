package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.TypeSafeConverter;

public interface HasConverters {

    public <T> StringConverter<T> getConverter(Class<T> aTo);

    public <F,T> TypeSafeConverter<F,T> getConverter(Class<F> aFromType, Class<T> aToType);

}
