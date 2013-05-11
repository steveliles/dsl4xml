package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.ConverterRegistry;
import com.sjl.dsl4xml.support.StringConverter;

/**
 * @author steve
 */
public interface Context extends ConverterRegistry
{
	void push(Object aContext);

	<T> T pop();

	<T> T peek();

	boolean hasNext();

	JsonToken next();

	boolean isStartObjectNamed(String aName);

	boolean isNotEndObject();

	boolean isStartArrayNamed(String aName);

	boolean isNotEndArray();

	boolean isPropertyNamed(String aName);

	void prepareForPossibleArrayEntry();

	void removeUnusedArrayEntry();

	String getValue();
}
