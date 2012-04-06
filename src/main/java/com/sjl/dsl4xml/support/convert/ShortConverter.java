package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class ShortConverter implements Converter<Short> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Short.class);
	}

	@Override
	public Short convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? null : new Short(aValue);
	}
	
}

