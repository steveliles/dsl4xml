package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class PrimitiveIntConverter implements Converter<Integer> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return Integer.TYPE.isAssignableFrom(aClass);
	}

	@Override
	public Integer convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : new Integer(aValue);
	}

}
