package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.support.Converter;

/**
 * @author steve
 */
public interface Context
{
	void push(Object aContext);

	<T> T pop();

	<T> T peek();

	boolean hasNext();

	JsonToken next();

	boolean isStartObjectNamed(String aName);

	boolean isStringNamed(String aName);

	boolean isNotEndObject(String aName);

	void registerConverters(Converter<?>... converters);
}
