package com.sjl.dsl4xml.support;

import java.lang.reflect.Method;

public interface Reflector {

    Method getMutator(Class<?> aClass, String aName, Object aValue);

}
