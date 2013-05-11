package com.sjl.dsl4xml.pull;

import java.util.*;

import com.sjl.dsl4xml.support.StringConverter;
import org.xmlpull.v1.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.convert.*;

public final class PullParserReadingContext implements ReadingContext {

	private XmlPullParser parser;
	private Stack<Object> stack;
	private List<TypeSafeConverter<?,?>> converters;
	
	public PullParserReadingContext(XmlPullParser aParser) {
		stack = new Stack<Object>();
		parser = aParser;
		converters = new ArrayList<TypeSafeConverter<?,?>>();
		
		registerConverters(
			new PrimitiveBooleanStringConverter(),
			new PrimitiveByteStringConverter(),
			new PrimitiveShortStringConverter(),
			new PrimitiveIntStringConverter(),
			new PrimitiveLongStringConverter(),
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
	public void registerConverters(StringConverter<?>... aConverters) {
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
			throw new ParsingException(anExc);
		}
	}
	
	@Override
	public int next() {
		try {
			return parser.next();
		} catch (Exception anExc) {
			throw new ParsingException(anExc);
		}
	}

	@Override
	public boolean isTagNamed(String aTagName) {
		return aTagName.equals(parser.getName());
	}

	@Override
	public boolean isNotEndTag(String aNamespacePrefix, String aTagName) {
		try {
			return !(
				(parser.getEventType() == XmlPullParser.END_TAG) && 
				(aTagName.equals(parser.getName())) &&
				(isMatchingNamespace(aNamespacePrefix, parser.getPrefix()))
			);
		} catch (XmlPullParserException anExc) {
			throw new ParsingException(anExc);
		}
	}

	@Override
	public boolean isStartTag() {
		try {
			return parser.getEventType() == XmlPullParser.START_TAG;
		} catch (XmlPullParserException anExc) {
			throw new ParsingException(anExc);
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
			throw new ParsingException(anExc);
		}
	}

	@Override
	public boolean isStartTagNamed(String aNamespacePrefix, String aTagName) {
		try {
			return (
				(parser.getEventType() == XmlPullParser.START_TAG) && 
				(aTagName.equals(parser.getName())) &&
				(isMatchingNamespace(aNamespacePrefix, parser.getPrefix()))
			);
		} catch (Exception anExc) {
			throw new ParsingException(anExc);
		}
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

	private boolean isMatchingNamespace(String aWantedPrefix, String aCurrentPrefix) {
		if ((aWantedPrefix == null) || ("".equals(aWantedPrefix))) {
			return (aCurrentPrefix == null);
		} else {
			return aWantedPrefix.equals(aCurrentPrefix);
		}
	}
}
