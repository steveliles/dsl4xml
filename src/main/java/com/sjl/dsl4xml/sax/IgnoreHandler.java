package com.sjl.dsl4xml.sax;

import org.xml.sax.*;

public class IgnoreHandler<T> implements Handler<T> {
	private Handler<T> parent;
	private IgnoreHandler<T> child;
	
	public IgnoreHandler(Handler<T> aParent) {
		parent = aParent;
	}

	@Override
	public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx) {
		if (child == null) {
			child = new IgnoreHandler<T>(this);
		}
		return child;
	}

	@Override
	public Handler<?> moveUp(String aQName, Context aCtx) {
		return parent;
	}

	@Override
	public Handler<?> characters(char[] aChars, int aStart, int aLength, Context aContext) {
		return this;
	}
}
