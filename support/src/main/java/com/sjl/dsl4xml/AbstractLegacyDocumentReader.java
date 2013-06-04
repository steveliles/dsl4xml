package com.sjl.dsl4xml;

import java.io.*;
import java.util.*;

import com.sjl.dsl4xml.support.*;

public abstract class AbstractLegacyDocumentReader<T> implements LegacyDocumentReader<T> {

	protected StringConverter<?>[] converters;
	protected Class<T> resultType;
	
	public AbstractLegacyDocumentReader(Class<T> aClass) {
		resultType = aClass;
	}
	
	public abstract T read(Reader aReader)
	throws ParsingException;
	
	public void registerConverters(StringConverter<?>... aConverters) {
		List<StringConverter<?>> _converters = new ArrayList<StringConverter<?>>();
		_converters.addAll(Arrays.asList(aConverters));
		if (converters != null)
			_converters.addAll(Arrays.asList(converters));
		converters = _converters.toArray(new StringConverter<?>[_converters.size()]);
	}

	public T read(InputStream anInputStream, String aCharSet) {
		return read(newReader(anInputStream, aCharSet));
	}
	
	protected T newResultObject() {
		return Classes.newInstance(resultType);
	}
	
	private Reader newReader(InputStream anInputStream, String aCharSet) {
		try {
			return new InputStreamReader(anInputStream, aCharSet);
		} catch (UnsupportedEncodingException anExc) {
			throw new RuntimeException(anExc);
		}
	}
}
