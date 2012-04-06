package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Integer.class);
	}

	@Override
	public Integer convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? null : new Integer(aValue);
	}
	
}
