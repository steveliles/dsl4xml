package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class PrimitiveByteConverter implements Converter<Byte> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Byte.TYPE);
	}

	@Override
	public Byte convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : new Byte(aValue);
	}

}
