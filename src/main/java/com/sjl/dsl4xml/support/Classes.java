package com.sjl.dsl4xml.support;

import java.lang.reflect.*;
import java.util.*;

public class Classes {

	public static <T> Method getMutatorMethod(Class<T> aClass, String aFieldName) {
		String _methodSuffix = upperCaseFirstLetter(aFieldName);
		return getMethod(aClass, _methodSuffix, "set", "add", "insert", "put");
	}
	
	private static String upperCaseFirstLetter(String aString) {
		return ("" + aString.charAt(0)).toUpperCase() + aString.substring(1, aString.length());
	}
	
	private static <T> Method getMethod(Class<T> aClass, String aSuffix, String... aPrefixes) {
		List<String> _names = new ArrayList<String>();
		for (String _pre : aPrefixes) {
			_names.add(_pre);
			_names.add(_pre + aSuffix);
		}
		
		for (String _name : _names) {
			for (Method _m : aClass.getMethods()) {
				if (_name.equals(_m.getName()) && (_m.getParameterTypes().length == 1)) {
					_m.setAccessible(true); // allow to invoke non-public methods
					return _m;
				}
			}
		}
		
		String _msg = "No mutator method found in class " + aClass.getName() + ", tried ";
		for (String _name : _names) {
			_msg += _name + ",";
		}
		throw new NoSuitableMethodException(_msg);
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
			new Class<?>[]{ aClass },
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
}
