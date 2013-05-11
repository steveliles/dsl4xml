package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

public interface ConverterRegistration<F,T> {

    public <R> ConverterRegistration<F,R> to(Class<R> aToType);

    public ConverterRegistration<F,T> using(Converter<F,T> aConverter);

    public Converter<F,T> get();

}
