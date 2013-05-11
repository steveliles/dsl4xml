package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.TypeSafeConverter;
import com.sjl.dsl4xml.support.StringConverter;
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
	private List<TypeSafeConverter<?,?>> converters;
	private Stack<Object> stack;
	private Stack<String> names;
	private String name;
	private String value;
	private JsonToken token;

	public GsonContext(JsonReader aReader)
	{
		reader = aReader;
		stack = new Stack<Object>();
		names = new Stack<String>();
		converters = new ArrayList<TypeSafeConverter<?,?>>();

		registerConverters(
			new PrimitiveBooleanStringConverter(),
			new DecimalToByteStringConverter(),
			new DecimalToShortStringConverter(),
			new DecimalToIntStringConverter(),
			new DecimalToLongStringConverter(),
			new PrimitiveCharStringConverter(),
			new PrimitiveFloatStringConverter(),
			new PrimitiveDoubleStringConverter(),
			new BooleanStringConverter(),
			new ByteStringConverter(),
			new ShortStringConverter(),
			new IntegerStringConverter(),
			new LongStringConverter(),
			new CharacterStringConverter(),
			new FloatStringConverter(),
			new DoubleStringConverter(),
			new ClassStringConverter(),
			new StringStringConverter()
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
	public void registerConverters(TypeSafeConverter<?,?>... aConverters)
	{
		// push any registered converters on ahead of existing converters (allows simple override)
		converters.addAll(0, Arrays.asList(aConverters));
	}

    public <T> StringConverter<T> getConverter(Class<T> aTo) {
        return (StringConverter<T>) getConverter(String.class, aTo);
    }

    public <F,T> TypeSafeConverter<F,T> getConverter(Class<F> aFromType, Class<T> aToType) {
        for (TypeSafeConverter<?,?> _c : converters) {
            if ((_c.canConvertFrom(aFromType)) && (_c.canConvertTo(aToType))) {
                return (TypeSafeConverter<F,T>) _c;
            }
        }

        throw new RuntimeException("No converter registered that can convert from " + aFromType + " to " + aToType);
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
			switch(token) {
				case NAME:
					value = null;
					name = names.push(reader.nextName());
					token = reader.peek();
					break;
				case BEGIN_OBJECT:
					value = null;
					reader.beginObject();
					break;
				case END_OBJECT:
					value = null;
					reader.endObject();
					if (!names.isEmpty()) {
						names.pop();
						if (!names.isEmpty())
							name = names.peek();
						else
							name = null;
					} else {
						name = null;
					}
					break;
				case BEGIN_ARRAY:
					reader.beginArray();
					value = null;
					token = reader.peek();
					break;
				case END_ARRAY:
					value = null;
					reader.endArray();
					if (!names.isEmpty()) {
						names.pop();
						if (!names.isEmpty())
							name = names.peek();
						else
							name = null;
					} else {
						name = null;
					}
					break;
				case STRING:
					value = reader.nextString();
					name = names.pop();
					break;
				case BOOLEAN:
					value = String.valueOf(reader.nextBoolean());
					name = names.pop();
					break;
				case NUMBER:
					value = String.valueOf(reader.nextDouble()); // todo: optimise
					name = names.pop();
					break;
				case NULL:
					value = null;
					reader.nextNull();
					name = names.pop();
					break;
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
	public boolean isNotEndObject() {
		return !(token == JsonToken.END_OBJECT);
	}

	@Override
	public boolean isStartArrayNamed(String aName) {
		return (
			(token == JsonToken.BEGIN_ARRAY) &&
			(aName.equals(name))
		);
	}

	@Override
	public boolean isNotEndArray() {
		return !(token == JsonToken.END_ARRAY);
	}

	@Override
	public boolean isPropertyNamed(String aName) {
		return (
			(token == JsonToken.BOOLEAN || token == JsonToken.STRING || token == JsonToken.NUMBER) &&
			(aName == null || aName.isEmpty() || aName.equals(name))
		);
	}

	@Override
	public void prepareForPossibleArrayEntry() {
		name = names.push("");
	}

	@Override
	public void removeUnusedArrayEntry() {
		names.pop();
		if (!names.isEmpty())
			name = names.peek();
	}

	@Override
	public String getValue() {
		return value;
	}
}
