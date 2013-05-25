package com.sjl.dsl4xml.support;

import java.lang.reflect.Method;

public interface Reflector {

    public <T> T newInstance(Class<T> aType);

    public Method getMutator(Class<?> aClass, String aName, Object aValue);

}
