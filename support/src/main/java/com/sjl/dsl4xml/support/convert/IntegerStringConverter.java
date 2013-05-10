package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

public class IntegerStringConverter extends StringConverter<Integer> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Integer.class);
	}

	@Override
	public Integer convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? null : new Integer(aValue);
	}
	
}
