package com.sjl.dsl4xml.support;

import java.lang.reflect.Method;

public class FixedCapabilityReflector implements Reflector {

    @Override
    public <T> T newInstance(Class<T> aType) {
        return null;  // TODO
    }

    @Override
    public Class<?> getExpectedType(Class<?> aClass, Name aName) {
        return null;  // TODO
    }

    @Override
    public Method getMutator(Class<?> aClass, Name aName, Object aValue) {
        return null;  // TODO
    }

    @Override
    public boolean hasMutator(Class<?> aClass, Name aName, Class<?> aValueType) {
        return false;  // TODO
    }
}
