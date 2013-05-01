package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class FloatConverter implements Converter<Float> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Float.class);
	}

	@Override
	public Float convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? null : new Float(aValue);
	}
	
}
