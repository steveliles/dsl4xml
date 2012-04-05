package com.sjl.dsl4xml;

import java.util.*;

import org.xmlpull.v1.*;

public class MappingContext {

	private XmlPullParser parser;
	private Stack<Object> stack;
	
	public MappingContext(XmlPullParser aParser) {
		stack = new Stack<Object>();
		parser = aParser;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T peek() {
		return (T) stack.peek();
	}
	
	public void push(Object aContext) {
		stack.push(aContext);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T pop() {
		return (T) stack.pop();
	}
	
	public XmlPullParser getParser() {
		return parser;
	}
	
	public boolean hasMoreTags() {
		try {
			return parser.getEventType() != XmlPullParser.END_DOCUMENT;
		} catch (XmlPullParserException anExc) {
			throw new XmlParseException(anExc);
		}
	}
	
	public int next() {
		try {
			return parser.next();
		} catch (Exception anExc) {
			throw new XmlParseException(anExc);
		}
	}

	public boolean isTagNamed(String aTagName) {
		return aTagName.equals(parser.getName());
	}

	public boolean isNotEndTag(String aTagName) {
		try {
			return !(
				(parser.getEventType() == XmlPullParser.END_TAG) && 
				(aTagName.equals(parser.getName()))
			);
		} catch (XmlPullParserException anExc) {
			throw new XmlParseException(anExc);
		}
	}

	public boolean isStartTag() {
		try {
			return parser.getEventType() == XmlPullParser.START_TAG;
		} catch (XmlPullParserException anExc) {
			throw new XmlParseException(anExc);
		}
	}
	
	public String getAttributeValue(String anAttributeName) {
		return parser.getAttributeValue(null, anAttributeName);
	}
	
	public boolean isTextNode() {
		try {
			return parser.getEventType() == XmlPullParser.TEXT;
		} catch (XmlPullParserException anExc) {
			throw new XmlParseException(anExc);
		}
	}
}
