package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

/**
 * A converter that can convert specific enum classes.
 * You must register a converter per enum class that you wish to convert.
 *
 * @author steve
 */
public class EnumStringConverter<T extends java.lang.Enum<T>> extends StringConverter<T> {

	private Class<T> enumClass;
	private boolean forceCaseInsensitivity;

	/**
	 * @param anEnumClass the concrete enum class to convert to
	 * @param aForceCaseInsensitivity - set to true if your xml/json expresses the values in lower or mixed case
	 */
	public EnumStringConverter(Class<T> anEnumClass, boolean aForceCaseInsensitivity) {
		if (!anEnumClass.isEnum())
			throw new IllegalArgumentException(anEnumClass + " is not an enum.");

		forceCaseInsensitivity = aForceCaseInsensitivity;

		enumClass = anEnumClass;
	}

	public EnumStringConverter(Class<T> anEnumClass) {
		this(anEnumClass, false); // prefer speed
	}

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(enumClass);
	}

	@Override
	public T convert(String aValue) {
		if (forceCaseInsensitivity) {
			return (aValue == null) ? null : Enum.valueOf(enumClass, aValue.toUpperCase());
		} else {
			return (aValue == null) ? null : Enum.valueOf(enumClass, aValue);
		}
	}
}
