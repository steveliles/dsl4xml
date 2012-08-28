package com.sjl.dsl4xml.sax;

import org.xml.sax.*;

public interface Handler<R> {
	
	public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx);
	
	public Handler<?> moveUp(String aQName, Context aCtx);
	
	public Handler<?> characters(char[] aChars, int aStart, int aLength, Context aContext);
	
}
