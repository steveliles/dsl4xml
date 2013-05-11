package com.sjl.dsl4xml.sax;

import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.StringConverter;
import com.sjl.dsl4xml.support.convert.*;

public class Context implements HasConverters {
	private Stack<Object> ctx;
	private Object result;
	private List<TypeSafeConverter<?,?>> converters;
	
	public Context(TypeSafeConverter<?,?>... aConverters) {
		converters = new ArrayList<TypeSafeConverter<?,?>>();
		
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

    public <T> StringConverter<T> getConverter(Class<T> aTo) {
        return (StringConverter<T>) getConverter(String.class, aTo);
    }

	public <F,T> TypeSafeConverter<F,T> getConverter(Class<F> aFromType, Class<T> aToType) {
		for (TypeSafeConverter<?,?> _c : converters) {
			if ((_c.canConvertFrom(aFromType)) && (_c.canConvertTo(aToType))) {
				return (TypeSafeConverter<F,T>) _c;
			}
		}
		throw new RuntimeException("No converter registered that can convert from " + aFromType + " to " + aToType);
	}

	@Override
	public void registerConverters(StringConverter<?>... aConverters) {
		// push any registered converters on ahead of existing converters (allows simple override)
		converters.addAll(0, Arrays.asList(aConverters));
	}
}