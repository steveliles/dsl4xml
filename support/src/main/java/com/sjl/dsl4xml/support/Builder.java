package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.Context;
import com.sjl.dsl4xml.Name;

public interface Builder<T> {

    public Name getName();

    public boolean isArray();

    public Class<? extends T> getTargetType();

    /**
     * @return the builder for a nested object
     */
    public Builder<?> moveDown(Context aContext);

    /**
     * Creates the build-context object, which may be a mutable intermediate type
     */
    public void prepare(Context aContext);

    /**
     * set a named value to the build-context object
     * @param aName
     * @param aValue
     */
    public void setValue(Context aContext, String aName, Object aValue);

    /**
     * build the final output, which may be a different type to the build-context
     * @return
     */
    public T build(Context aContext);

}
