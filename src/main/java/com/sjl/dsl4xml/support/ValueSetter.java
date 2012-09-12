package com.sjl.dsl4xml.support;

import java.lang.reflect.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.Classes.NoSuitableMethodException;

public class ValueSetter {
	private Method method;
	private Converter<?> converter;
	
	public ValueSetter(HasConverters aConverters, Class<?> aClass, String aFieldName) {
		method = getMethod(aClass, aFieldName);
		converter = aConverters.getConverter(getArgType(method));
	}
	
	public void invoke(Object anOn, String aWith) {
		try {
			method.invoke(anOn, converter.convert(aWith));
		} catch (XmlReadingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new RuntimeException(anExc);
		}
	}
	
	public String toString() {
		return method.getDeclaringClass().getSimpleName() + "." + method.getName();
	}
	
	private Method getMethod(Class<?> aClass, String aFieldName) {
		Method _m = Classes.getMutatorMethod(aClass, aFieldName);
		
		Class<?>[] _params = _m.getParameterTypes();
		if (_params.length != 1) {
			throw new NoSuitableMethodException("Mutator method " + aClass.getSimpleName() + "." + _m.getName() + " should accept 1 param, but wants " + _params.length);
		}	
		return _m;
	}
	
	private Class<?> getArgType(Method aMethod) {
		return aMethod.getParameterTypes()[0];
	}
}

