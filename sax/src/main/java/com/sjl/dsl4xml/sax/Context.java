package com.sjl.dsl4xml.sax;

import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;
import com.sjl.dsl4xml.support.convert.*;

public class Context implements HasConverters {
	private Stack<Object> ctx;
	private Object result;
	private List<Converter<?>> converters;
	
	public Context(Converter<?>... aConverters) {
		converters = new ArrayList<Converter<?>>();
		
		registerConverters(
			new PrimitiveBooleanConverter(),
			new PrimitiveByteConverter(),
			new PrimitiveShortConverter(),
			new PrimitiveIntConverter(),
			new PrimitiveLongConverter(),
			new PrimitiveCharConverter(),
			new PrimitiveFloatConverter(),
			new PrimitiveDoubleConverter(),
			new BooleanConverter(),
			new ByteConverter(),
			new ShortConverter(),
			new IntegerConverter(),
			new LongConverter(),
			new CharacterConverter(),
			new FloatConverter(),
			new DoubleConverter(),
			new ClassConverter(),
			new StringConverter()
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
		return (ctx == null) ? null : ctx.peek();
	}
	
	public Object pop() {	
		return (ctx == null) ? null : ctx.pop();
	}
	
	public Object getResult() {
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getConverter(Class<T> aArgType) {
		for (Converter<?> _c : converters) {
			if (_c.canConvertTo(aArgType)) {
				return (Converter<T>) _c;
			}
		}
		throw new RuntimeException("No converter registered that can convert to " + aArgType);
	}

	@Override
	public void registerConverters(Converter<?>... aConverters) {
		// push any registered converters on ahead of existing converters (allows simple override)
		converters.addAll(0, Arrays.asList(aConverters));
	}
}