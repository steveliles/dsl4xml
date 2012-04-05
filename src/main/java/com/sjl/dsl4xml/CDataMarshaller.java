package com.sjl.dsl4xml;

import java.lang.reflect.*;

import com.sjl.dsl4xml.support.*;

public final class CDataMarshaller<T> implements Marshaller {

	private String fieldName;
	private Method mutator;

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

	@SuppressWarnings("unchecked")
	public void map(MarshallingContext aContext, String aText) {
		T _ctx = aContext.peek();
		
		try {
			getMutator((Class<T>)_ctx.getClass(), fieldName).invoke(_ctx, aText); // TODO: type conversion
		} catch (Exception anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}
	
	private Method getMutator(Class<T> aClass, String aFieldName) {
		if (mutator == null) {
			mutator = Classes.getMutatorMethod(aClass, aFieldName);
		}
		return mutator;
	}

}
