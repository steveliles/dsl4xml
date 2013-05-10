package com.sjl.dsl4xml.sax;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import com.sjl.dsl4xml.support.*;

public class Dsl4XmlContentHandler<R> extends DefaultHandler {
	
	private Context context;
	private Handler<?> root;
	private Handler<?> handler;
	
	public Dsl4XmlContentHandler(DocHandler<R> aRootTagHandler) {
		root = aRootTagHandler;
		handler = aRootTagHandler;
	}
	
	public void prepare(StringConverter<?>... aConverters) {
		context = new Context(aConverters);
		handler = root;
	}
	
	@SuppressWarnings("unchecked")
	public R getResult() {
		return (R) context.getResult();
	}

	@Override
	public void startElement(
		String aUri, String aLocalName, 
		String aQName, Attributes anAttributes) 
	throws SAXException {
		handler = handler.startTag(aQName, anAttributes, context);
	}
	
	@Override
	public void endElement(
		String aUri, String aLocalName, String aQName
	) throws SAXException {
		handler = handler.moveUp(aQName, context);
	}

	@Override
	public void characters(char[] aChars, int aStart, int aLength)
	throws SAXException {
		handler = handler.characters(aChars, aStart, aLength, context);
	}
}