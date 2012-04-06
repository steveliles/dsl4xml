package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class PrimitiveFloatConverter implements Converter<Float> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Float.TYPE);
	}

	@Override
	public Float convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : new Float(aValue);
	}

}
