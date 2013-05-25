package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.ParsingException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class ReflectorFactory {

    public static final String MAGIC_SET = "__magic_set";
    // TODO: expose these so that they can be customised?
    public static final String[] ACCESSOR_PREFIXES = {"get", "is", "to"};
    public static final String[] MUTATOR_PREFIXES = {"add", "set", "insert", "put", MAGIC_SET};

    private Object lock;
    private Map<String,Method> cache;

    public ReflectorFactory() {
        lock = new Object();
        cache = new HashMap<String,Method>();
    }

    // TODO: separate Reflector from ReflectorFactory - the interfaces are different!
    public Reflector newReflector()
    {
        final Map<String,Method> methods = new HashMap<String,Method>(cache);

        return new Reflector()
        {
            @Override
            public <T> T newInstance(Class<T> aType) {
                try {
                    if (aType.isInterface()) {
                        return newDynamicProxy(aType);
                    } else {
                        return aType.newInstance();
                    }
                } catch (Exception anExc) {
                    throw new ParsingException(anExc);
                }
            }

            @Override
            public Method getMutator(Class<?> aClass, String aName, Object aValue) {
                return methods.get(aName);
            }
        };
    }

    public boolean prepare(Class<?> aClass, Name aName, Class<?> aValueType) {
        Method _m = cache.get(aName.getName());
        if (_m == null) {
            synchronized(lock) {
                _m = cache.get(aName.getName());
                // doesn't actually matter that DCL is broken, as worst case
                // we replace the cached method with the same method
                if (_m == null) {
                    _m = getMutatorMethod(aClass, aName); // TODO: use the value for disambiguation?
System.out.println("found mutator: " + _m.getName());
                    if (_m != null) {
                        Map<String,Method> _newCache = new HashMap<String,Method>(cache);
                        _newCache.put(aName.getName(), _m);
                        cache = _newCache;
                    }
                }
            }
        }
        return _m != null;
    }

    public static <T> Class<T> getProxy(Class<T> aClass) {
        if (aClass.isInterface()) {
            return (Class<T>)Proxy.getProxyClass(
                Classes.class.getClassLoader(),
                new Class<?>[]{ aClass, Mutable.class }
            );
        } else {
            return aClass;
        }
    }

    public static Class<?> getExpectedType(Class<?> aClass, Name aName) {
        // TODO: probably need some checks here, e.g. number of params?
        Class<?> _type = getAccessorMethod(aClass, aName).getReturnType();
        if (_type.isInterface()) {
            System.out.println("interface, proxying as " + getProxy(_type));
            return getProxy(_type);
        } else {
            System.out.println("not interface, returning " + _type.getName());
            return _type;
        }
    }

    public static <T,P> boolean hasMutatorMethod(Class<T> aContextClass, Name aName, Class<P> aParamType) {
        Method _m = getMutatorMethod(aContextClass, aName);
        if (_m != null) {
            for (Class<?> _t : _m.getParameterTypes()) {
                if (_t.isAssignableFrom(aParamType))
                    return true;
            }
        }
        return false;
    }

    private static <T> Method getAccessorMethod(Class<T> aClass, Name... aMaybeNames) {
        Set<String> _names = new LinkedHashSet<String>();
        for (Name _s : aMaybeNames) {
            String _name = (_s.getAlias() == null) ? _s.getName() : _s.getAlias();
            String _suffix = removeHyphensAndUpperCaseFirstLetters(_name);
            for (String _prefix : ACCESSOR_PREFIXES) {
                _names.add(_prefix + _suffix);
            }
        }

        for (String _s : ACCESSOR_PREFIXES) {
            _names.add(_s);
        }

        return getMethod(aClass, _names, true);
    }

    private static <T> Method getMutatorMethod(Class<T> aContextClass, Name... aMaybeNames) {
        Set<String> _names = new LinkedHashSet<String>();
        for (Name _s : aMaybeNames) {
            String _name = (_s.getAlias() == null) ? _s.getName() : _s.getAlias();
            String _aliasSuffix = removeHyphensAndUpperCaseFirstLetters(_name);
            for (String _prefix : MUTATOR_PREFIXES) {
                _names.add(_prefix + _aliasSuffix);
            }
        }

        for (String _s : MUTATOR_PREFIXES) {
            _names.add(_s);
        }
        _names.add(MAGIC_SET); // always look for the magic set method for dynamic proxies

        return getMethod(aContextClass, _names, true);
    }

    private static String removeHyphensAndUpperCaseFirstLetters(String aString) {
        if (aString == null || "".equals(aString))
            return "";

        String[] _parts = aString.split("-");
        StringBuilder _sb = new StringBuilder(aString.length());
        for (int i=0; i<_parts.length; i++) {
            _sb.append(Character.toUpperCase(_parts[i].charAt(0)));
            _sb.append(_parts[i].substring(1, _parts[i].length()));
        }
        return _sb.toString();
    }

    private static <T> Method getMethod(Class<T> aContextClass, Collection<String> aNames, boolean aThrowExceptionIfNotFound) {
        List<Method> _methods = new ArrayList<Method>();
        List<Method> _twoArgMethods = new ArrayList<Method>();
        for (Method _m : aContextClass.getMethods()) {
            if (_m.getParameterTypes().length <= 1) {
                _methods.add(_m);
            } else if (_m.getParameterTypes().length == 2) {
                _twoArgMethods.add(_m);
            }
        }
        _methods.addAll(_twoArgMethods); // prefer single arg methods, so put them at the front of the list

        for (String _name : aNames) {
            for (Method _m : _methods) {
                if (_name.equals(_m.getName())) {
                    _m.setAccessible(true); // allow to invoke non-public methods
                    return _m;
                }
            }
        }

        if (aThrowExceptionIfNotFound) {
            String _classname =
                (aContextClass.isAnonymousClass() ||
                 aContextClass.isSynthetic() ||
                 aContextClass.getName().matches(".*\\$Proxy.*")) ?
                    asString(aContextClass, aContextClass.getInterfaces()) : aContextClass.getName();

            String _msg = "No suitable method found in class " + _classname + ", tried ";
            for (String _name : aNames) {
                _msg += _name + ",";
            }
            throw new NoSuitableMethodException(_msg);
        } else {
            return null;
        }
    }

    private static String asString(Class<?> aClass, Class<?>... aClasses) {
        return aClass.getName() + Arrays.asList(aClasses).toString();
    }

    private static <T> T newDynamicProxy(Class<T> aClass) {
        if (Iterable.class.isAssignableFrom(aClass)) {
            return newListBasedProxy(aClass);
        } else {
            return newMapBasedProxy(aClass);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T newListBasedProxy(final Class<T> aClass) {
        return (T) Proxy.newProxyInstance(
            Classes.class.getClassLoader(),
            new Class<?>[]{aClass, Mutable.class},
            new ListBasedInvocationHandler<T>(aClass)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T> T newMapBasedProxy(final Class<T> aClass) {
        return (T) Proxy.newProxyInstance(
            Classes.class.getClassLoader(),
            new Class<?>[]{ aClass, Mutable.class },
            new MapBasedInvocationHandler(aClass)
        );
    }

    private static class MapBasedInvocationHandler implements InvocationHandler {

        private Map<String, Object> map = new HashMap<String, Object>();
        private Class<?> clz;

        public MapBasedInvocationHandler(Class<?> aClass) {
            clz = aClass;
        }

        @Override
        public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {
            if ("toString".equals(aMethod.getName())) {
                return toString();
            } else if ("hashCode".equals(aMethod.getName())) {
                return hashCode();
            } else if ("equals".equals(aMethod.getName())) {
                return equals(aArgs[0]);
            } else if (isMutator(aMethod)) {
                if (aArgs.length == 2) {
                    return map.put(aArgs[0].toString(), aArgs[1]);
                } else {
                    return map.put(getSuffix(aMethod.getName()), aArgs[0]);
                }
            } else {
                return map.get(getSuffix(aMethod.getName()));
            }
        }

        private boolean isMutator(Method aMethod) {
            return (aMethod.getParameterTypes().length > 0);
        }

        // TODO: cache?
        private String getSuffix(String aMethodName) {
            int _indexOfFirstUpperCase = indexOfUpperCase(aMethodName);
            if (_indexOfFirstUpperCase < 0)
                return aMethodName;

            return aMethodName.substring(
                _indexOfFirstUpperCase, _indexOfFirstUpperCase+1).toLowerCase() +
                aMethodName.substring(_indexOfFirstUpperCase+1);
        }

        private int indexOfUpperCase(String aMethodName) {
            for (int i=0; i<aMethodName.length(); i++) {
                if (Character.isUpperCase(aMethodName.charAt(i)))
                    return i;
            }
            return -1;
        }

        public String toString() {
            return "proxy(" + clz.getName() + ")" + map;
        }
    }

    private static class ListBasedInvocationHandler<T> implements InvocationHandler {

        private Map<String, Object> map = new HashMap<String, Object>();
        private List<T> list = new ArrayList<T>();
        private Class<?> clz;

        public ListBasedInvocationHandler(Class<?> aClass) {
            clz = aClass;
        }

        @Override
        public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {
            try {
                if ("toString".equals(aMethod.getName())) {
                    return toString();
                } else if ("hashCode".equals(aMethod.getName())) {
                    return hashCode();
                } else if ("equals".equals(aMethod.getName())) {
                    return equals(aArgs[0]);
                } else {
                    if (isMethodOf(List.class, aMethod)) {
                        return aMethod.invoke(list, aArgs);
                    } else {
                        if (isMutator(aMethod)) {
                            if (aArgs.length == 2) {
                                return map.put(aArgs[0].toString(), aArgs[1]);
                            } else {
                                return map.put(getSuffix(aMethod.getName()), aArgs[0]);
                            }
                        } else {
                            return map.get(getSuffix(aMethod.getName()));
                        }
                    }
                }
            } catch (InvocationTargetException anExc) {
                throw anExc.getCause();
            } catch (IllegalAccessException anExc) {
                throw anExc;
            }
        }

        private boolean isMethodOf(Class<?> aClass, Method aMethod) {
            return aMethod.getDeclaringClass().isAssignableFrom(aClass);
        }

        private boolean isMutator(Method aMethod) {
            return (aMethod.getParameterTypes().length > 0);
        }

        private String getSuffix(String aMethodName) {
            return aMethodName.substring(3); // TODO
        }

        public String toString() {
            return "proxy(" + clz.getName() + ")" + list;
        }
    }
}
