package com.sjl.dsl4xml;

import com.sjl.dsl4xml.support.Builder;

public interface Definition<T> {

    public <R extends T> Builder<R> newBuilder();

}
