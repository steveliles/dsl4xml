package com.sjl.dsl4xml.support;

import java.lang.reflect.*;

import com.sjl.dsl4xml.*;

@Deprecated
public class ValueSetter {

	private Method setter;
	private StringConverter<?> converter;
	private boolean twoArgSetter;
	private Method getter;

	public ValueSetter(ConverterRegistry aConverters, Class<?> aContextType, String... aMaybeNames) {
		this(aConverters, aContextType, null, aMaybeNames);
	}

	public ValueSetter(ConverterRegistry aConverters, Class<?> aContextType, Class<?> aValueType, String... aMaybeNames) {
		setter = getMethod(aContextType, aMaybeNames);
		getter = Classes.getAccessorMethod(aContextType, aMaybeNames);
		if (getter != null) {
			if (aValueType != null) {
				converter = aConverters.getConverter(aValueType);
			} else {
				converter = aConverters.getConverter(getter.getReturnType());
			}
		} else {
			if (aValueType != null) {
				converter = aConverters.getConverter(aValueType);
			} else {
				converter = aConverters.getConverter(setter.getParameterTypes()[0]);
			}
		}
		twoArgSetter = (setter.getParameterTypes().length == 2);
	}
	
	public void invoke(String aKey, Object anOn, String aWith) {
		try {
			if (twoArgSetter) {
				setter.invoke(anOn, aKey, converter.convert(aWith));
			} else {
				setter.invoke(anOn, converter.convert(aWith));
			}
		} catch (ParsingException anExc) {
			throw anExc;
        } catch (IllegalArgumentException anExc) {
            throw new ParsingException(
                anExc.getMessage() +
                " while trying to invoke " + setter +
                " on " + anOn +
                " with " + ((aKey != null) ? aKey + " and " : "") + aWith +
                " converted by " + converter, anExc);
		} catch (Exception anExc) {
			throw new RuntimeException(anExc);
		}
	}
	
	public String toString() {
		return setter.getDeclaringClass().getSimpleName() + "." + setter.getName();
	}
	
	private Method getMethod(Class<?> aContextType, String... aMaybeNames) {
		Method _m = Classes.getMutatorMethod(aContextType, aMaybeNames);
		
		Class<?>[] _params = _m.getParameterTypes();
		if ((_params.length == 1) || (Classes.MAGIC_SET.equals(_m.getName()) && _params.length==2)) {
			return _m;
		} else {
			throw new NoSuitableMethodException("Mutator method " + aContextType.getSimpleName() + "." + _m.getName() + " should accept 1 param, but wants " + _params.length);
		}
	}
}

