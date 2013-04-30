package com.sjl.dsl4xml.sax;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

public class TextHandler {
	
	private ValueSetter setter;
	private String field;
	private StringBuilder chars;
	
	public TextHandler(String aFieldName) {
		field = aFieldName;
		chars = new StringBuilder();
	}
	
	public void handle(char[] aChars, int aStart, int aLength, Context aContext) {
		chars.append(aChars, aStart, aLength);
	}
	
	public void complete(Context aContext) {
		Object _currentContext = aContext.peek();	
		try {
			ValueSetter _vs = getSetter(aContext, _currentContext.getClass(), field);
			_vs.invoke(field, _currentContext, chars.toString());
			chars.setLength(0);
		} catch (XmlReadingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	private ValueSetter getSetter(Context aContext, Class<?> aClass, String aFieldName) {
		if (setter == null) {
			setter = new ValueSetter(aContext, aClass, aFieldName);
		}
		return setter;
	}	
}
