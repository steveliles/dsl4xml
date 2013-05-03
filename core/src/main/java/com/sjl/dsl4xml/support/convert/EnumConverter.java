package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.Converter;

/**
 * A converter that can convert specific enum classes.
 * You must register a converter per enum class that you wish to convert.
 *
 * @author steve
 */
public class EnumConverter<T extends java.lang.Enum<T>> implements Converter<T> {
	private Class<T> enumClass;

	public EnumConverter(Class<T> anEnumClass) {
		if (!anEnumClass.isEnum())
			throw new IllegalArgumentException(anEnumClass + " is not an enum.");

		enumClass = anEnumClass;
	}

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(enumClass);
	}

	@Override
	public T convert(String aValue) {
		return (aValue == null) ? null : Enum.valueOf(enumClass, aValue);
	}
}
