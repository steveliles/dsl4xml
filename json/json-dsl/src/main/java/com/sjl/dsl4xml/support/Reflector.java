package com.sjl.dsl4xml.support;

import java.lang.reflect.Method;

public interface Reflector {

    public <T> T newInstance(Class<T> aType);

    public Class<?> getExpectedType(Class<?> aClass, Name aName);

    public Method getMutator(Class<?> aClass, Name aName, Object aValue);

    public boolean hasMutator(Class<?> aClass, Name aName, Class<?> aValueType);

}
