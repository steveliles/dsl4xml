package com.sjl.dsl4xml.support;

import java.util.List;

public interface Context {

    public <T> T build(Builder<T> aBuilder);

    public Builder<?> select(List<Builder<?>> aBuilders);

    public void push(Object anObject);

    public <T> T pop();

    public <T> T peek();

}
