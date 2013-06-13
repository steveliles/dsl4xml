package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.ReflectorFactory;

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

        private Array<R> delegate;

        public Impl(Class<? extends R> aType) {
            delegate = new Array<R>(Name.MISSING, aType);
        }

        @Override
        public UnNamedArray<R> of(UnNamedProperty<?,?> aContent) {
            delegate.of(aContent);
            return this;
        }

        @Override
        public UnNamedArray<R> of(UnNamedObject<?> aContent) {
            delegate.of(aContent);
            return this;
        }

        @Override
        public UnNamedArray<R> of(UnNamedArray<?> aContent) {
            delegate.of(aContent);
            return this;
        }

        @Override
        public void onAttach(Class<?> aContainerType, ReflectorFactory aReflector, ConverterRegistry aConverters) {
            delegate.onAttach(aContainerType, aReflector, aConverters);
        }

        @Override
        public Name getName() {
            return delegate.getName();
        }

        @Override
        @SuppressWarnings("rawtypes")
        public Builder<R> newBuilder() {
            return delegate.newBuilder();
        }
    };
}
