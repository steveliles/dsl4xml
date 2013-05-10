package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

/**
 * @author steve
 */
public class DecimalToByteStringConverter implements StringConverter<Byte>
{
	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Byte.TYPE);
	}

	@Override
	public Byte convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : convertNonEmpty(aValue);
	}

	private Byte convertNonEmpty(String aValue) {
		return (aValue.endsWith(".0")) ? new Byte(aValue.substring(0, aValue.length()-2)) : new Byte(aValue);
	}
}
