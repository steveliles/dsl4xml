package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

public class BooleanStringConverter extends StringConverter<Boolean> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Boolean.class);
	}

	@Override
	public Boolean convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? Boolean.FALSE : Boolean.valueOf(aValue);
	}
	
}

