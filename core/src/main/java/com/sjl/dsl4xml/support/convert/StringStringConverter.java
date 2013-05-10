package com.sjl.dsl4xml.support.convert;

public class StringStringConverter implements com.sjl.dsl4xml.support.StringConverter<String> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(String.class);
	}

	@Override
	public final String convert(String aValue) {
		return aValue;
	}
	
}
