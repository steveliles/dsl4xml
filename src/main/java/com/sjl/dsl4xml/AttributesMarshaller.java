package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.*;

public class AttributesMarshaller implements Marshaller {

	private String[] attributeNames;
	private ValueSetter[] mutators;
	
	public AttributesMarshaller(String... anAttributeNames) {
		attributeNames = anAttributeNames;
		mutators = new ValueSetter[anAttributeNames.length];
	}
	
	@Override
	public boolean map(MarshallingContext aContext) {
		Object _currentContext = aContext.peek();
		for (int i=0; i<attributeNames.length; i++) {
			
			if (mutators[i] == null) {
				mutators[i] = new ValueSetter(aContext, _currentContext.getClass(), attributeNames[i]);
			}
			
			mutators[i].invoke(
				_currentContext,
				aContext.getAttributeValue(attributeNames[i])
			);
		}
		return false;
	}
}
