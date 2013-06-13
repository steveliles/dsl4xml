package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Content;
import com.sjl.dsl4xml.ConverterRegistry;
import com.sjl.dsl4xml.Definition;
import com.sjl.dsl4xml.Name;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.ReflectorFactory;

public interface NamedArray<T> extends Content<T>, Definition<T> {

    /**
     * Specify a simple property type for this array. This is a shortcut for of(UnNamedProperty(aConvertableType))
     *
     * @param aConvertableType a class for which a (String->ConvertableType) converter is registered.
     *
     * @return this
     */
    public NamedArray<T> of(Class<?> aConvertableType);

    public NamedArray<T> of(UnNamedProperty<?,?> aContent);

    public NamedArray<T> of(UnNamedObject<?> aContent);

    public NamedArray<T> of(UnNamedArray<?> aContent);

    public abstract class Impl<R> implements NamedArray<R> {

        private Array<R> delegate;

        public Impl(Name aName, Class<? extends R> aType) {
            delegate = new Array<R>(aName, aType);
        }

        @Override
        public NamedArray<R> of(UnNamedProperty<?,?> aContent) {
            delegate.of(aContent);
            return this;
        }

        @Override
        public NamedArray<R> of(UnNamedObject<?> aContent) {
            delegate.of(aContent);
            return this;
        }

        @Override
        public NamedArray<R> of(UnNamedArray<?> aContent) {
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
        public Builder<R> newBuilder() {
            return delegate.newBuilder();
        }
    };
}
