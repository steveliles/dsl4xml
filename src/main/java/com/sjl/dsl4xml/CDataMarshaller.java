package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.*;

public final class CDataMarshaller<T> implements Marshaller {

	private String fieldName;
	private ValueSetter setter;

	public CDataMarshaller(String aFieldName) {
		fieldName = aFieldName;
	}
	
	@Override
	public final boolean map(MarshallingContext aContext) {
		if (aContext.isTextNode()) {
			map(aContext, aContext.getParser().getText());
			return true;
		} else {
			return false;
		}
	}

	public void map(MarshallingContext aContext, String aText) {
		T _currentContext = aContext.peek();
		
		try {
			ValueSetter _vs = getSetter(aContext, _currentContext.getClass(), fieldName);
			_vs.invoke(_currentContext, aText);
		} catch (Exception anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}
	
	private ValueSetter getSetter(MarshallingContext aContext, Class<?> aClass, String aFieldName) {
		if (setter == null) {
			setter = new ValueSetter(aContext, aClass, aFieldName);
		}
		return setter;
	}	
}
