package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Context;
import com.sjl.dsl4xml.Name;

public class NoResultBuilder<T> implements Builder<T> {
    private Name name;
    private Stack<Name> names

    public NoResultBuilder(Name aName) {
        name = aName;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public Class<? extends T> getTargetType() {
        return null;
    }

    @Override
    public Builder<?> moveDown(Context aContext) {
        return this;
    }

    @Override
    public void prepare(Context aContext) {
        aContext.push(null);
    }

    @Override
    public void setValue(Context aContext, String aName, Object aValue) {
    }

    @Override
    public T build(Context aContext) {
        return aContext.pop();
    }
}
