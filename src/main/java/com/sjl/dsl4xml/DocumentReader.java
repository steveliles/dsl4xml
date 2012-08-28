package com.sjl.dsl4xml;

import java.io.*;

import com.sjl.dsl4xml.support.*;

public interface DocumentReader<T> {

	public abstract T read(Reader aReader) 
	throws XmlReadingException;

	public abstract T read(InputStream anInputStream, String aCharSet)
	throws XmlReadingException;
	
	public abstract void registerConverters(Converter<?>... aConverters);
}