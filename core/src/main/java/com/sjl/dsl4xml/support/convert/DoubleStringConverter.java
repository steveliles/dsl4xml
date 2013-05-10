package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

public class DoubleStringConverter implements StringConverter<Double> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Double.class);
	}

	@Override
	public Double convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? null : new Double(aValue);
	}
	
}

