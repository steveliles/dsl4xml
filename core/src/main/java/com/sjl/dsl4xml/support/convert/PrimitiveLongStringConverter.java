package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

public class PrimitiveLongStringConverter extends StringConverter<Long> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Long.TYPE);
	}

	@Override
	public Long convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : new Long(aValue);
	}

}
