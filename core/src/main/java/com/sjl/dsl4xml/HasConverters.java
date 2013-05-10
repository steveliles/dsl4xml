package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.*;

public interface HasConverters {

    public abstract void registerConverters(StringConverter<?>... aConverters);
	
	public abstract <T> StringConverter<T> getConverter(Class<T> aArgType);
	
}
