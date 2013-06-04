package com.sjl.dsl4xml;

import java.io.*;

import com.sjl.dsl4xml.support.*;

public interface LegacyDocumentReader<T> {

	public abstract T read(Reader aReader) 
	throws ParsingException;

	public abstract T read(InputStream anInputStream, String aCharSet)
	throws ParsingException;
	
	public abstract void registerConverters(StringConverter<?>... aConverters);
}