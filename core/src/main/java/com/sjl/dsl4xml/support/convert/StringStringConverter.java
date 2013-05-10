package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

public class StringStringConverter extends StringConverter<String> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(String.class);
	}

	@Override
	public final String convert(String aValue) {
		return aValue;
	}
	
}
