package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.support.Converter;
import com.sjl.dsl4xml.support.convert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @author steve
 */
public class GsonContext implements Context
{
	private JsonReader reader;
	private List<Converter<?>> converters;
	private Stack<Object> stack;

	private String name;
	private JsonToken token;

	public GsonContext(JsonReader aReader)
	{
		reader = aReader;
		stack = new Stack<Object>();
		converters = new ArrayList<Converter<?>>();

		registerConverters(
			new PrimitiveBooleanConverter(),
			new PrimitiveByteConverter(),
			new PrimitiveShortConverter(),
			new PrimitiveIntConverter(),
			new PrimitiveLongConverter(),
			new PrimitiveCharConverter(),
			new PrimitiveFloatConverter(),
			new PrimitiveDoubleConverter(),
			new BooleanConverter(),
			new ByteConverter(),
			new ShortConverter(),
			new IntegerConverter(),
			new LongConverter(),
			new CharacterConverter(),
			new FloatConverter(),
			new DoubleConverter(),
			new ClassConverter(),
			new StringConverter()
		);
	}

	@Override
	public void push(Object aContext) {
		stack.push(aContext);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T pop() {
		return (T) stack.pop();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T peek() {
		return (T) stack.peek();
	}

	@Override
	public void registerConverters(Converter<?>... aConverters)
	{
		// push any registered converters on ahead of existing converters (allows simple override)
		converters.addAll(0, Arrays.asList(aConverters));
	}

	@Override
	public boolean hasNext() {
		try {
			return reader.peek() != JsonToken.END_DOCUMENT;
		} catch (IOException anExc) {
			throw new ParsingException(anExc);
		}
	}

	@Override
	public JsonToken next() {
		try {
			token = reader.peek();
System.out.println(token.name());
			if ((token == JsonToken.BEGIN_OBJECT) || (token == JsonToken.BEGIN_ARRAY)) {
				reader.beginObject();
				if (reader.peek() == JsonToken.NAME)
					name = reader.nextName();
			} else if (token == JsonToken.END_OBJECT) {
				reader.endObject();
			} else if (token == JsonToken.END_ARRAY) {
				reader.endArray();
			}
			return token;
		} catch (IOException anExc) {
			throw new ParsingException(anExc);
		}
	}

	@Override
	public boolean isStartObjectNamed(String aName) {
		return (
			(token == JsonToken.BEGIN_OBJECT) &&
			(aName.equals(name))
		);
	}

	@Override
	public boolean isStringNamed(String aName) {
		return (
			(token == JsonToken.STRING) &&
			(aName.equals(name))
		);
	}

	@Override
	public boolean isNotEndObject(String aName) {
		return !(
			(token == JsonToken.END_OBJECT) &&
			(aName.equals(name))
		);
	}
}
