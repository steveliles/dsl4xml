package com.sjl.dsl4xml;

import java.io.*;

import javax.xml.parsers.*;

import org.xml.sax.*;

import com.sjl.dsl4xml.sax.*;

public class SAXLegacyDocumentReader<T> extends AbstractLegacyDocumentReader<T> {

	public static <R> SAXLegacyDocumentReader<R> mappingOf(String aTagName, Class<R> aClass) {
		return new SAXLegacyDocumentReader<R>(
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
	
	public SAXLegacyDocumentReader(Class<T> aResultType, TagHandler<T> aRootTagHandler) {
		super(aResultType);
		try {
			SAXParserFactory _f = SAXParserFactory.newInstance();
		    SAXParser _p = _f.newSAXParser();
		    
		    root = aRootTagHandler;
		    reader = _p.getXMLReader();
		    handler = new Dsl4XmlContentHandler<T>(new DocHandler<T>(aRootTagHandler));
		    reader.setContentHandler(handler);
		} catch (Exception anExc) {
			throw new ParsingException(anExc);
		}
	}
	
	public SAXLegacyDocumentReader<T> to(AttributesHandler anAttributes) {
		root.with(anAttributes);
		return this;
	}
	
	public SAXLegacyDocumentReader<T> to(TagHandler<?>... aHandlers) {
		root.to(aHandlers);
		return this;
	}
	
	public SAXLegacyDocumentReader<T> to(AttributesHandler anAttributes, TagHandler<?>... aHandlers) {
		root.with(anAttributes, aHandlers);
		return this;
	}
	
	@Override
	public T read(Reader aReader) throws ParsingException
	{
		handler.prepare(converters);
		try {
			reader.parse(new InputSource(aReader));
			return handler.getResult();
		} catch (ParsingException anExc) {
			throw anExc;
		} catch (Exception anExc) {		
			throw new ParsingException(anExc);
		}
	}
}
