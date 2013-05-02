package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.Converter;

/**
 * @author steve
 */
public class DecimalToShortConverter implements Converter<Short>
{
	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Short.TYPE);
	}

	@Override
	public Short convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : convertNonEmpty(aValue);
	}

	private Short convertNonEmpty(String aValue) {
		return (aValue.endsWith(".0")) ? new Short(aValue.substring(0, aValue.length()-2)) : new Short(aValue);
	}
}
