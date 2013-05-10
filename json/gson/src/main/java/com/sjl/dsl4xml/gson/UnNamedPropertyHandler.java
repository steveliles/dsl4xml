package com.sjl.dsl4xml.gson;

/**
 * @author steve
 */
public class UnNamedPropertyHandler<T>
{
	private PropertyHandler<T> handler;

	public UnNamedPropertyHandler(Class<T> aPropertyType) {
		handler = new PropertyHandler<T>(null, aPropertyType);
	}

	public PropertyHandler<T> get() {
		return handler;
	}
}
