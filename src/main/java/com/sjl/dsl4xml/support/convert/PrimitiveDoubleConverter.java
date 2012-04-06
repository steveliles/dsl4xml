package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class PrimitiveDoubleConverter implements Converter<Double> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Double.TYPE);
	}

	@Override
	public Double convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : new Double(aValue);
	}

}
