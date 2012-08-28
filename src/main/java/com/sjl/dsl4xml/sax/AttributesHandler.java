package com.sjl.dsl4xml.sax;

import org.xml.sax.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

public class AttributesHandler {
	private String[] attributeNames;
	private ValueSetter[] mutators;
	
	public AttributesHandler(String... anAttributeNames) {
		attributeNames = anAttributeNames;
		mutators = new ValueSetter[anAttributeNames.length];
	}
	
	public boolean handle(HasConverters aHasConverters, Object aCtx, Attributes anAttributes) {
		if (aCtx != null) {
			for (int i=0; i<attributeNames.length; i++) {
				ValueSetter _vs = getValueSetter(i, aHasConverters, aCtx.getClass());
				_vs.invoke(aCtx, anAttributes.getValue(attributeNames[i]));
			}
		}
		return false;
	}
	
	private ValueSetter getValueSetter(int anIndex, HasConverters aHasConverters, Class<?> aCurrentContextClass) {
		if (mutators[anIndex] == null) {
			mutators[anIndex] = new ValueSetter(aHasConverters, aCurrentContextClass, attributeNames[anIndex]);
		}
		return mutators[anIndex];
	}
}