package com.sjl.dsl4xml.pull;

import com.sjl.dsl4xml.support.*;

public class NamedAttributesReader implements AttributesReader {

	private String[] attributeNames;
	private ValueSetter[] mutators;
	
	public NamedAttributesReader(String... anAttributeNames) {
		attributeNames = anAttributeNames;
		mutators = new ValueSetter[anAttributeNames.length];
	}
	
	@Override
	public boolean read(ReadingContext aContext) {
		if (aContext.isStartTag()) {
			
			Object _currentContext = aContext.peek();
			for (int i=0; i<attributeNames.length; i++) {
				String _value = aContext.getAttributeValue(attributeNames[i]);
				if (_value != null) {
					ValueSetter _vs = getValueSetter(i, aContext, _currentContext.getClass());				
					_vs.invoke(attributeNames[i], _currentContext, _value);
				}
			}
		}
		return false;
	}
	
	private ValueSetter getValueSetter(int anIndex, ReadingContext aContext, Class<?> aCurrentContextClass) {
		if (mutators[anIndex] == null) {
			mutators[anIndex] = new ValueSetter(aContext, aCurrentContextClass, attributeNames[anIndex]);
		}
		return mutators[anIndex];
	}
}
