package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class PrimitiveShortConverter implements Converter<Short> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Short.TYPE);
	}

	@Override
	public Short convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : new Short(aValue);
	}

}
