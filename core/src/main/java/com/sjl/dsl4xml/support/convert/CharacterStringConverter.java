package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

public class CharacterStringConverter implements StringConverter<Character> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Character.class);
	}

	@Override
	public Character convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? null : aValue.charAt(0);
	}
	
}

