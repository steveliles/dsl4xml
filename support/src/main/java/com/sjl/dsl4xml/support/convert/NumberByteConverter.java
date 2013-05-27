package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

public class NumberByteConverter implements TypeSafeConverter<Number,Byte> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return
            aClass.isAssignableFrom(Byte.class) || // object wrapper
            aClass.isAssignableFrom(Byte.TYPE);    // primitive
    }

    @Override
    public Byte convert(Number aFrom) {
        if (aFrom == null)
            return null;

        if (aFrom instanceof Byte)
            return (Byte) aFrom;

        return aFrom.byteValue();
    }
}
