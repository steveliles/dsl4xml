package com.sjl.dsl4xml.sax;

import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.StringConverter;
import com.sjl.dsl4xml.support.convert.*;

public class Context implements HasConverters {
	private Stack<Object> ctx;
	private Object result;
	private List<StringConverter<?>> converters;
	
	public Context(StringConverter<?>... aConverters) {
		converters = new ArrayList<StringConverter<?>>();
		
		registerConverters(
			new PrimitiveBooleanStringConverter(),
			new PrimitiveByteStringConverter(),
			new PrimitiveShortStringConverter(),
			new PrimitiveIntStringConverter(),
			new PrimitiveLongStringConverter(),
			new PrimitiveCharStringConverter(),
			new PrimitiveFloatStringConverter(),
			new PrimitiveDoubleStringConverter(),
			new BooleanStringConverter(),
			new ByteStringConverter(),
			new ShortStringConverter(),
			new IntegerStringConverter(),
			new LongStringConverter(),
			new CharacterStringConverter(),
			new FloatStringConverter(),
			new DoubleStringConverter(),
			new ClassStringConverter(),
			new StringStringConverter()
		);
		
		if (aConverters != null)
			converters.addAll(0, Arrays.asList(aConverters));
	}
	
	public void push(Object anObject) {
		if (ctx == null) {
			ctx = new Stack<Object>();
			result = anObject;
		}
		ctx.push(anObject);			
	}
	
	public Object peek() {
		return (ctx == null) ? null : (ctx.isEmpty()) ? null : ctx.peek();
	}
	
	public Object pop() {	
		return (ctx == null) ? null : (ctx.isEmpty()) ? null : ctx.pop();
	}
	
	public Object getResult() {
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> StringConverter<T> getConverter(Class<T> aArgType) {
		for (StringConverter<?> _c : converters) {
			if (_c.canConvertTo(aArgType)) {
				return (StringConverter<T>) _c;
			}
		}
		throw new RuntimeException("No converter registered that can convert to " + aArgType);
	}

	@Override
	public void registerConverters(StringConverter<?>... aConverters) {
		// push any registered converters on ahead of existing converters (allows simple override)
		converters.addAll(0, Arrays.asList(aConverters));
	}
}