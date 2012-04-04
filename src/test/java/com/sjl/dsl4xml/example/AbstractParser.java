package com.sjl.dsl4xml.example;

import java.io.*;

import com.sjl.dsl4xml.*;

public abstract class AbstractParser<T> {

	protected DocumentMapper<T> mapper;
	
	protected abstract DocumentMapper<T> defineMapper();
	
	protected abstract T newResultObject();
	
	@SuppressWarnings("unchecked")
	public <R extends AbstractParser<T>> R init() {
		mapper = defineMapper();
		return (R)this;
	}
	
	public T parse(InputStream anInputStream) {
		final T _result = newResultObject();
		mapper.map(
			newUTF8Reader(anInputStream), 
			new MappingContext(_result)
		);
		return _result;
	}
	
	private Reader newUTF8Reader(InputStream anInputStream) {
		try {
			return new InputStreamReader(anInputStream, "utf-8");
		} catch (UnsupportedEncodingException anExc) {
			throw new RuntimeException(anExc);
		}
	}
	
}
