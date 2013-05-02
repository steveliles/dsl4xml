package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.Converter;

/**
 * @author steve
 */
public class DecimalToIntConverter implements Converter<Integer>
{
	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Integer.TYPE);
	}

	@Override
	public Integer convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : convertNonEmpty(aValue);
	}

	private Integer convertNonEmpty(String aValue) {
		return (aValue.endsWith(".0")) ? new Integer(aValue.substring(0, aValue.length()-2)) : new Integer(aValue);
	}
}
