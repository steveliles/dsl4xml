package com.sjl.dsl4xml;

import org.xmlpull.v1.*;

public class MappingContext {

	private XmlPullParser parser;
	private Object result;
	
	public MappingContext(Object aResult) {
		result = aResult;
	}
	
	public Object getResult() {
		return result;
	}
	
	public XmlPullParser getParser() {
		return parser;
	}
	
	public void setParser(XmlPullParser aParser) {
		parser = aParser;
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
	
	public boolean isTextNode() {
		try {
			return parser.getEventType() == XmlPullParser.TEXT;
		} catch (XmlPullParserException anExc) {
			throw new XmlParseException(anExc);
		}
	}
}
