package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.*;

public interface HasConverters {

    public abstract void registerConverters(Converter<?>... aConverters);
	
	public abstract <T> Converter<T> getConverter(Class<T> aArgType);
	
}
