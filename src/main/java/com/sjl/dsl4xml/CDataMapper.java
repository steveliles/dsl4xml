package com.sjl.dsl4xml;

public abstract class CDataMapper implements Mapper {

	@Override
	public boolean map(MappingContext aContext) {
		if (aContext.isTextNode()) {
			map(aContext, aContext.getParser().getText());
			return true;
		} else {
			return false;
		}
	}

	public abstract void map(MappingContext aContext, String aText);
}
