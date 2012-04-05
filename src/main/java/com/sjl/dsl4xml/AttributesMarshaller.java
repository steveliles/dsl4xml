package com.sjl.dsl4xml;

import java.lang.reflect.*;

import com.sjl.dsl4xml.support.*;

public class AttributesMarshaller implements Marshaller {

	private AttributeMutator[] mutators;
	
	public AttributesMarshaller(String... anAttributeNames) {
		mutators = new AttributeMutator[anAttributeNames.length];
		for (int i=0; i<anAttributeNames.length; i++) {
			mutators[i] = new AttributeMutator(anAttributeNames[i]);
		}
	}
	
	@Override
	public boolean map(MappingContext aContext) {
		for (AttributeMutator _am : mutators) {
			_am.mutate(
				aContext.peek(),
				aContext.getAttributeValue(_am.attributeName)
			);
		}
		return false;
	}
	
	class AttributeMutator {
		private String attributeName;
		private Method mutator;
		public AttributeMutator(String anAttributeName) {
			attributeName = anAttributeName;
		}
		
		public void mutate(Object anObject, String aValue) {
			if (mutator == null) {
				mutator = Classes.getMutatorMethod(anObject.getClass(), attributeName);
			}
			
			try {
				mutator.invoke(anObject, aValue);
			} catch (Exception anExc) {
				throw new XmlParseException(anExc);
			}
		}
	}
}
