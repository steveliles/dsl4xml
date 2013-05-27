package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

import java.math.BigInteger;

public class NumberBigIntegerConverter implements TypeSafeConverter<Number,BigInteger> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return aClass.isAssignableFrom(BigInteger.class);
    }

    @Override
    public BigInteger convert(Number aFrom) {
        if (aFrom == null)
            return null;

        if (aFrom instanceof BigInteger)
            return (BigInteger) aFrom;

        // only need to convert from the types available
        // in javascript, so long is good enough
        return BigInteger.valueOf(aFrom.longValue());
    }
}
