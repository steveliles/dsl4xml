package com.sjl.dsl4xml.sax;

import org.xml.sax.*;

public class DocHandler<R> implements Handler<R> {

	private TagHandler<R> root;
	
	public DocHandler(TagHandler<R> aRoot) {
		root = aRoot;
	}
	
	@Override
	public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx) {			
		if (root.handlesTag(aQName)) {
			return root.moveDown(aQName, anAttributes, aCtx);
		} else {
			throw new InvalidStateException("unexpected root element: " + aQName);
		}
	}

	@Override
	public Handler<?> moveUp(String aQName, Context aCtx) {
		return root;
	}

	@Override
	public Handler<?> characters(char[] aChars, int aStart, int aLength, Context aContext) {
		return root.characters(aChars, aStart, aLength, aContext);
	}
}

