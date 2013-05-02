package com.sjl.dsl4xml.support;

import java.lang.reflect.*;

import com.sjl.dsl4xml.*;

public class ValueSetter {
	private Method method;
	private Converter<?> converter;
	private boolean twoArgSetter;

private Method _getter;
	public ValueSetter(HasConverters aConverters, Class<?> aClass, String... aMaybeNames) {
		method = getMethod(aClass, aMaybeNames);
		_getter = Classes.getAccessorMethod(aClass, aMaybeNames);
		if (_getter != null) {
			converter = aConverters.getConverter(_getter.getReturnType());
		} else {
			converter = aConverters.getConverter(method.getParameterTypes()[0]);
		}
		twoArgSetter = (method.getParameterTypes().length == 2);
	}
	
	public void invoke(String aKey, Object anOn, String aWith) {
		try {
			if (twoArgSetter) {
				method.invoke(anOn, aKey, converter.convert(aWith));
			} else {
				method.invoke(anOn, converter.convert(aWith));
			}
		} catch (ParsingException anExc) {
			throw anExc;
        } catch (IllegalArgumentException anExc) {
            throw new ParsingException(
                anExc.getMessage() +
                " while trying to invoke " + method +
                " on " + anOn +
                " with " + ((aKey != null) ? aKey + " and " : "") + aWith +
                " converted by " + converter, anExc);
		} catch (Exception anExc) {
			throw new RuntimeException(anExc);
		}
	}
	
	public String toString() {
		return method.getDeclaringClass().getSimpleName() + "." + method.getName();
	}
	
	private Method getMethod(Class<?> aClass, String... aMaybeNames) {
		Method _m = Classes.getMutatorMethod(aClass, aMaybeNames);
		
		Class<?>[] _params = _m.getParameterTypes();
		if ((_params.length == 1) || (Classes.MAGIC_SET.equals(_m.getName()) && _params.length==2)) {
			return _m;
		} else {
			throw new NoSuitableMethodException("Mutator method " + aClass.getSimpleName() + "." + _m.getName() + " should accept 1 param, but wants " + _params.length);
		}
	}
}

