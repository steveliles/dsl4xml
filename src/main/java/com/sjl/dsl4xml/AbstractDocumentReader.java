package com.sjl.dsl4xml;

import java.io.*;
import java.util.*;

import com.sjl.dsl4xml.support.*;

public abstract class AbstractDocumentReader<T> {

	protected Converter<?>[] converters;
	protected Class<T> resultType;
	
	public AbstractDocumentReader(Class<T> aClass) {
		resultType = aClass;
	}
	
	public abstract T read(Reader aReader)
	throws XmlReadingException;
	
	public void registerConverters(Converter<?>... aConverters) {
		List<Converter<?>> _converters = new ArrayList<Converter<?>>();
		_converters.addAll(Arrays.asList(aConverters));
		if (converters != null)
			_converters.addAll(Arrays.asList(converters));
		converters = _converters.toArray(new Converter<?>[_converters.size()]);
	}

	public T read(InputStream anInputStream, String aCharSet) {
		return read(newReader(anInputStream, aCharSet));
	}
	
	protected T newResultObject() {
		try {
			return resultType.newInstance();
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	private Reader newReader(InputStream anInputStream, String aCharSet) {
		try {
			return new InputStreamReader(anInputStream, aCharSet);
		} catch (UnsupportedEncodingException anExc) {
			throw new RuntimeException(anExc);
		}
	}
}
