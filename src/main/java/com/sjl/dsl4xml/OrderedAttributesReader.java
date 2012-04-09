package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.*;

public class OrderedAttributesReader implements XmlReader {

	private String[] methodNames;
	private ValueSetter[] mutators;
	
	public OrderedAttributesReader(String... aSetterMethodNames) {
		methodNames = aSetterMethodNames;
		mutators = new ValueSetter[aSetterMethodNames.length];
	}
	
	@Override
	public boolean read(ReadingContext aContext) {
		if (aContext.isStartTag()) {
			Object _currentContext = aContext.peek();
			for (int i=0; i<methodNames.length; i++) {
				
				if (mutators[i] == null) {
					mutators[i] = new ValueSetter(aContext, _currentContext.getClass(), methodNames[i]);
				}
				
				mutators[i].invoke(
					_currentContext,
					aContext.getAttributeValue(i)
				);
			}
		}
		return false;
	}
}
