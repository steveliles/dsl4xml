package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.ReflectiveBuilder;
import com.sjl.dsl4xml.support.ReflectorFactory;

import java.util.ArrayList;
import java.util.Collections;

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

    public abstract class Impl<R> implements UnNamedArray<R>{

        private Array<R> impl;

        public Impl(Array<R> anImpl) {
            impl = anImpl;
        }

        @Override
        public UnNamedArray<R> of(UnNamedProperty<?,?> aContent) {
            impl.of(aContent);
            return this;
        }

        @Override
        public UnNamedArray<R> of(UnNamedObject<?> aContent) {
            impl.of(aContent);
            return this;
        }

        @Override
        public UnNamedArray<R> of(UnNamedArray<?> aContent) {
            impl.of(aContent);
            return this;
        }

        @Override
        public void onAttach(Class<?> aContainerType, ReflectorFactory aReflector, ConverterRegistry aConverters) {
            impl.onAttach(aContainerType, aReflector, aConverters);
        }

        @Override
        public Name getName() {
            return impl.getName();
        }

        @Override
        @SuppressWarnings("rawtypes")
        public Builder<R> newBuilder() {
            return impl.newBuilder();
        }
    };
}
