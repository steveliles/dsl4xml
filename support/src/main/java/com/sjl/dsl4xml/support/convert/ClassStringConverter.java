package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.StringConverter;

public class ClassStringConverter extends StringConverter<Class<?>> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return Class.class.isAssignableFrom(aClass);
	}

	@Override
	public Class<?> convert(String aValue) {
		try {
			return ((aValue == null) || ("".equals(aValue)) ? null : Class.forName(aValue));
		} catch (ClassNotFoundException anExc) {
			throw new ParsingException(anExc);
		}
	}

}
