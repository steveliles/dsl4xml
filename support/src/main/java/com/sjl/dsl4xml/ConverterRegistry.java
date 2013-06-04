package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.*;

public interface ConverterRegistry {

    public abstract void registerConverters(TypeSafeConverter<?,?>... aConverters);

    public abstract <F,T> TypeSafeConverter<F,T> getConverter(Class<F> aFromType, Class<T> aToType);
	
	public abstract <T> StringConverter<T> getConverter(Class<T> aArgType);
	
}
