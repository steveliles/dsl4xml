package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.ReflectorFactory;

public interface NamedObject<T> extends Content<T>, Definition<T> {

    public <I> NamedObject<I> via(Class<I> anIntermediateType);

    public <I> NamedObject<I> via(Class<I> anIntermediateType, Converter<I,T> aConverter);

    public NamedObject<T> with(Content<?> ... aContent);

    class Impl<R> implements NamedObject<R> {
        private JsonObject<R> delegate;

        public Impl(Name aName, Class<? extends R> aType) {
            delegate = new JsonObject<R>(aName, aType);
        }

        @Override
        public <I> NamedObject<I> via(Class<I> anIntermediateType) {
            delegate.via(anIntermediateType);
            return (NamedObject<I>) this;
        }

        @Override
        public <I> NamedObject<I> via(Class<I> anIntermediateType, Converter<I, R> aConverter) {
            delegate.via(anIntermediateType, aConverter);
            return (NamedObject<I>) this;
        }

        @Override
        public NamedObject<R> with(Content<?>... aContent) {
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
