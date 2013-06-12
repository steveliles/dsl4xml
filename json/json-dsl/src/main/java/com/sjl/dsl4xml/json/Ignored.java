package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Content;
import com.sjl.dsl4xml.ConverterRegistry;
import com.sjl.dsl4xml.Name;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.NoResultBuilder;
import com.sjl.dsl4xml.support.ReflectorFactory;

public interface Ignored<A> extends Content<A> {

    public class Impl<R> implements Ignored<R> {
        private Name name;

        public Impl(Name aName) {
            name = aName;
        }

        @Override
        public Name getName() {
            return name;
        }

        @Override
        public void onAttach(Class<?> aContainerType, ReflectorFactory aFactory, ConverterRegistry aConverters) {}

        @Override
        public Builder<R> newBuilder() {
            return new NoResultBuilder<R>(name);
        }
    };

}
