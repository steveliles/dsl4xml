package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;

public interface UnNamedArray<T> extends Content<T>, Definition<T> {

    /**
     * Specify a simple property type for this array. This is a shortcut for of(UnNamedProperty(aConvertableType))
     *
     * @param aConvertableType a class for which a (String->ConvertableType) converter is registered.
     *
     * @return this
     */
    public UnNamedArray<T> of (Class<?> aConvertableType);

    public UnNamedArray<T> of(UnNamedProperty<?,?> aContent);

    public UnNamedArray<T> of(UnNamedObject<?> aContent);

    public UnNamedArray<T> of(UnNamedArray<?> aContent);

    public <I> UnNamedArray<I> via(Class<I> anIntermediateType);

    public <I> UnNamedArray<I> via(Class<I> anIntermediateType, Converter<I,T> aConverter);

}
