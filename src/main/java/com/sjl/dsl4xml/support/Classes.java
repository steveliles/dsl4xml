package com.sjl.dsl4xml.support;

import java.lang.reflect.*;

public class Classes {


	public static <T> Method getMutatorMethod(Class<T> aClass, String aFieldName) {
		String _methodSuffix = upperCaseFirstLetter(aFieldName);
		return getMethod(aClass, _methodSuffix, "set", "add", "insert", "put");
	}
	
	private static String upperCaseFirstLetter(String aString) {
		return ("" + aString.charAt(0)).toUpperCase() + aString.substring(1, aString.length());
	}
	
	private static <T> Method getMethod(Class<T> aClass, String aSuffix, String... aPrefixes) {
		for (String _pre : aPrefixes) {
			String _name = _pre + aSuffix;
			for (Method _m : aClass.getMethods()) {
				if (_name.equals(_m.getName())) {
					_m.setAccessible(true); // allow to invoke non-public methods
					return _m;
				}
			}
		}
		
		String _msg = "No mutator method found in class " + aClass.getName() + ", tried ";
		for (String _pre : aPrefixes) {
			_msg += _pre + aSuffix + ",";
		}
		throw new NoSuitableMethodException(_msg);
	}
	
	static class NoSuitableMethodException extends RuntimeException {
		public NoSuitableMethodException(String aMessage) {
			super(aMessage);
		}
	}
}
