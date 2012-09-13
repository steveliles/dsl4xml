package com.sjl.dsl4xml.support;

import java.lang.reflect.*;
import java.util.*;

import com.sjl.dsl4xml.*;

public class Classes {

    public static final String[] MUTATOR_PREFIXES = {"set", "add", "insert", "put"};
    
	public static <T> Method getMutatorMethod(Class<T> aClass, String... aMaybeNames) {
	    List<String> _names = new ArrayList<String>();
	    for (String _s : aMaybeNames) {
	        String _suffix = upperCaseFirstLetter(_s);	        
	        for (String _prefix : MUTATOR_PREFIXES) {
	            _names.add(_prefix);
	            _names.add(_prefix + _suffix);
	        }	        
	    }	 
	    return getMethod(aClass, _names);
	}
	
	private static String upperCaseFirstLetter(String aString) {
		return ("" + aString.charAt(0)).toUpperCase() + aString.substring(1, aString.length());
	}
	
	private static <T> Method getMethod(Class<T> aClass, List<String> aNames) {
	    for (String _name : aNames) {
			for (Method _m : aClass.getMethods()) {
				if (_name.equals(_m.getName()) && (_m.getParameterTypes().length == 1)) {
					_m.setAccessible(true); // allow to invoke non-public methods
					return _m;
				}
			}
		}
		
	    String _classname = 
            (aClass.isAnonymousClass() || aClass.isSynthetic() || aClass.getName().startsWith("$Proxy")) ? 
                asString(aClass, aClass.getInterfaces()) : aClass.getName();
           
       String _msg = "No mutator method found in class " + _classname + ", tried ";
       for (String _name : aNames) {
           _msg += _name + ",";
       }
       throw new NoSuitableMethodException(_msg);
	}
	
	public static <T> T newInstance(Class<T> aClass) {
	    try {
            if (aClass.isInterface()) {
                return (T) newDynamicProxy(aClass);
            } else {        
                return (T) aClass.newInstance();
            }
        } catch (Exception anExc) {
            throw new XmlReadingException(anExc);
        }
	}
	
	public static <T> T newDynamicProxy(Class<T> aClass) {
		if (Iterable.class.isAssignableFrom(aClass)) {
			return newListBasedProxy(aClass);
		} else {		
			return newMapBasedProxy(aClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newListBasedProxy(final Class<T> aClass) {
		return (T) Proxy.newProxyInstance(
			aClass.getClassLoader(),
			new Class<?>[]{ aClass },
			new InvocationHandler() {
				private List<T> data = new ArrayList<T>();
				@Override
				public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {
					try {					
						return aMethod.invoke(data, aArgs);
					} catch (InvocationTargetException anExc) {
						throw anExc.getCause();
					}
				}
			});
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newMapBasedProxy(Class<T> aClass) {
		return (T) Proxy.newProxyInstance(
			aClass.getClassLoader(),
			new Class<?>[]{ aClass, Comparable.class },
			new InvocationHandler() {
			    private Map<String, Object> data = new HashMap<String, Object>();
				@Override
				public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {
					if (isMutator(aMethod)) {
						return data.put(getSuffix(aMethod.getName()), aArgs[0]);
					} else {
						return data.get(getSuffix(aMethod.getName()));
					}				    
				}
				
				private boolean isMutator(Method aMethod) {
					return (aMethod.getParameterTypes().length > 0);
				}
				
				private String getSuffix(String aMethodName) {
					return aMethodName.substring(3); // TODO
				}
			});
	}
	
	static class NoSuitableMethodException extends RuntimeException {
		public NoSuitableMethodException(String aMessage) {
			super(aMessage);
		}
	}
	
	private static String asString(Class<?> aClass, Class<?>... aClasses) {
		return aClass.getName() + Arrays.asList(aClasses).toString();
	}
}
