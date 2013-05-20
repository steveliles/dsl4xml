package com.sjl.dsl4xml.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ThreadSafeCachingReflector implements Reflector {

    private Object lock;
    private Map<String,Method> cache;

    public ThreadSafeCachingReflector() {
        lock = new Object();
        cache = new HashMap<String, Method>();
    }

    @Override
    public Method getMutator(Class<?> aClass, String aName, Object aValue) {
        Method _m = cache.get(aName);
        if (_m == null) {
            synchronized(lock) {
                _m = cache.get(aName);
                // doesn't actually matter that DCL is broken, as worst case
                // we replace the cached method with the same method
                if (_m == null) {
                    _m = Classes.getMutatorMethod(aClass, aName); // TODO: use the value for disambiguation
                    if (_m == null) {
                        throw new NoSuitableMethodException(
                            "Can't find an appropriate mutating method in " + aClass.getName() +
                            " for property named " + aName + " of type " + aValue.getClass());
                    } else {
                        Map<String,Method> _newCache = new HashMap<String,Method>(cache);
                        _newCache.put(aName, _m);
                        cache = _newCache;
                    }
                }
            }
        }
        return _m;
    }
}
