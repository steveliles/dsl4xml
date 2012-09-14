package com.sjl.dsl4xml;

import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.sjl.dsl4xml.sax.*;

public class SAXDocumentReader<T> extends AbstractDocumentReader<T> {

	public static <R> SAXDocumentReader<R> mappingOf(String aTagName, Class<R> aClass) {
		return new SAXDocumentReader<R>(
			aClass, new TagHandler<R>(aTagName, aClass)
		);
	}
	
	public static <R> TagHandler<R> tag(String aTagName) {
		return new TagHandler<R>(aTagName);
	}
	
	public static <R> TagHandler<R> tag(String aTagName, Class<R> aClass) {
		return new TagHandler<R>(aTagName, aClass);
	}
	
	public static AttributesHandler attributes(String... anAttributeNames) {
		return new AttributesHandler(anAttributeNames);
	}
	
	private XMLReader reader;
	private TagHandler<T> root;
	private Dsl4XmlContentHandler<T> handler;
	
	public SAXDocumentReader(Class<T> aResultType, TagHandler<T> aRootTagHandler) {
		super(aResultType);
		try {
			SAXParserFactory _f = SAXParserFactory.newInstance();
		    SAXParser _p = _f.newSAXParser();
		    
		    root = aRootTagHandler;
		    reader = _p.getXMLReader();
		    handler = new Dsl4XmlContentHandler<T>(new DocHandler<T>(aRootTagHandler));
		    reader.setContentHandler(handler);
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	public SAXDocumentReader<T> to(AttributesHandler anAttributes) {
		root.with(anAttributes);
		return this;
	}
	
	public SAXDocumentReader<T> to(TagHandler<?>... aHandlers) {
		root.to(aHandlers);
		return this;
	}
	
	public SAXDocumentReader<T> to(AttributesHandler anAttributes, TagHandler<?>... aHandlers) {
		root.with(anAttributes, aHandlers);
		return this;
	}
	
	@Override
	public T read(Reader aReader) throws XmlReadingException {
		handler.prepare(converters);
		try {
			reader.parse(new InputSource(aReader));
			return handler.getResult();
		} catch (XmlReadingException anExc) {
			throw anExc;
		} catch (Exception anExc) {		
			throw new XmlReadingException(anExc);
		}
	}
}
