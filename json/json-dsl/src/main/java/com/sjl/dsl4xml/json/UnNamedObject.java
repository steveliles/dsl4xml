package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.ReflectorFactory;

public interface UnNamedObject<T> extends Content<T>, Definition<T> {

    public <I> UnNamedObject<I> via(Class<I> anIntermediateType);

    public <I> UnNamedObject<I> via(Class<I> anIntermediateType, Converter<I,T> aConverter);

    public UnNamedObject<T> with(Content<?> ... aContent);

    class Impl<R> implements UnNamedObject<R> {
        private JsonObject<R> delegate;

        public Impl(Class<? extends R> aType) {
            delegate = new JsonObject<R>(Name.MISSING, aType);
        }

        @Override
        public <I> UnNamedObject<I> via(Class<I> anIntermediateType) {
            delegate.via(anIntermediateType);
            return (UnNamedObject<I>) this;
        }

        @Override
        public <I> UnNamedObject<I> via(Class<I> anIntermediateType, Converter<I, R> aConverter) {
            delegate.via(anIntermediateType, aConverter);
            return (UnNamedObject<I>) this;
        }

        @Override
        public UnNamedObject<R> with(Content<?>... aContent) {
            delegate.with(aContent);
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
    }
}