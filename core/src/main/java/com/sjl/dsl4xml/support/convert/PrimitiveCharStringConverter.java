package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

public class PrimitiveCharStringConverter extends StringConverter<Character> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Character.TYPE);
	}

	@Override
	public Character convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : aValue.charAt(0);
	}

}
