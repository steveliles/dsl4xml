package com.sjl.dsl4xml;

import java.util.*;

import org.xmlpull.v1.*;

import com.sjl.dsl4xml.support.*;
import com.sjl.dsl4xml.support.convert.*;

public final class PullParserReadingContext implements ReadingContext {

	private XmlPullParser parser;
	private Stack<Object> stack;
	private List<Converter<?>> converters;
	
	public PullParserReadingContext(XmlPullParser aParser) {
		stack = new Stack<Object>();
		parser = aParser;
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
	public void registerConverters(Converter<?>... aConverters) {
		// push any registered converters on ahead of existing converters (allows simple override)
		converters.addAll(0, Arrays.asList(aConverters));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T peek() {
		return (T) stack.peek();
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
	public XmlPullParser getParser() {
		return parser;
	}
	
	@Override
	public boolean hasMoreTags() {
		try {
			return parser.getEventType() != XmlPullParser.END_DOCUMENT;
		} catch (XmlPullParserException anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	@Override
	public int next() {
		try {
			return parser.next();
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}

	@Override
	public boolean isTagNamed(String aTagName) {
		return aTagName.equals(parser.getName());
	}

	@Override
	public boolean isNotEndTag(String aTagName) {
		try {
			return !(
				(parser.getEventType() == XmlPullParser.END_TAG) && 
				(aTagName.equals(parser.getName()))
			);
		} catch (XmlPullParserException anExc) {
			throw new XmlReadingException(anExc);
		}
	}

	@Override
	public boolean isStartTag() {
		try {
			return parser.getEventType() == XmlPullParser.START_TAG;
		} catch (XmlPullParserException anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	@Override
	public String getAttributeValue(String anAttributeName) {
		return parser.getAttributeValue(null, anAttributeName);
	}
	
	@Override
	public String getAttributeValue(int anIndex) {
		return parser.getAttributeValue(anIndex);
	}
	
	@Override
	public boolean isTextNode() {
		try {
			return parser.getEventType() == XmlPullParser.TEXT;
		} catch (XmlPullParserException anExc) {
			throw new XmlReadingException(anExc);
		}
	}

	@Override
	public boolean isStartTagNamed(String aTagName) {
		try {
			return (
				(parser.getEventType() == XmlPullParser.START_TAG) && 
				(aTagName.equals(parser.getName()))
			);
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getConverter(Class<T> aArgType) {
		for (Converter<?> _c : converters) {
			if (_c.canConvertTo(aArgType)) {
				return (Converter<T>) _c;
			}
		}
		throw new RuntimeException("No converter registered that can convert to " + aArgType);
	}
}
