package com.sjl.dsl4xml.support;

public interface Converter<T> {

	public boolean canConvertTo(Class<?> aClass);
	
	public T convert(String aValue);
	
}
