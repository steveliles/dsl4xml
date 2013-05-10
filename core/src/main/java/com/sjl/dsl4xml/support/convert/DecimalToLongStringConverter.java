package com.sjl.dsl4xml.support.convert;

/**
 * @author steve
 */
public class DecimalToLongStringConverter implements com.sjl.dsl4xml.support.StringConverter<Long>
{
	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Long.TYPE);
	}

	@Override
	public Long convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? 0 : convertNonEmpty(aValue);
	}

	private Long convertNonEmpty(String aValue) {
		return (aValue.endsWith(".0")) ? new Long(aValue.substring(0, aValue.length()-2)) : new Long(aValue);
	}
}
