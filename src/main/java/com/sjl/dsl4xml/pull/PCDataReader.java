package com.sjl.dsl4xml.pull;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

public final class PCDataReader<T> implements ContentReader {

	private String fieldName;
	private ValueSetter setter;

	public PCDataReader(String aFieldName) {
		fieldName = aFieldName;
	}
	
	@Override
	public final boolean read(ReadingContext aContext) {
		if (aContext.isTextNode()) {
			read(aContext, aContext.getParser().getText());
			return true;
		} else {
			return false;
		}
	}

	public void read(ReadingContext aContext, String aText) {
		T _currentContext = aContext.peek();
		
		try {
			ValueSetter _vs = getSetter(aContext, _currentContext.getClass(), fieldName);
			_vs.invoke(_currentContext, aText);
		} catch (XmlReadingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	private ValueSetter getSetter(ReadingContext aContext, Class<?> aClass, String aFieldName) {
		if (setter == null) {
			setter = new ValueSetter(aContext, aClass, aFieldName);
		}
		return setter;
	}	
}
