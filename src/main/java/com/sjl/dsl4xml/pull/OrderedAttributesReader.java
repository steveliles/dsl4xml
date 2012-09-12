package com.sjl.dsl4xml.pull;

import com.sjl.dsl4xml.support.*;

public class OrderedAttributesReader implements AttributesReader {

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
				ValueSetter _vs = getValueSetter(i, aContext, _currentContext.getClass());
				_vs.invoke(_currentContext, aContext.getAttributeValue(i));
			}
		}
		return false;
	}
	
	private ValueSetter getValueSetter(int anIndex, ReadingContext aContext, Class<?> aCurrentContextClass) {
		if (mutators[anIndex] == null) {
			mutators[anIndex] = new ValueSetter(aContext, aCurrentContextClass, methodNames[anIndex]);
		}
		return mutators[anIndex];
	}
}
