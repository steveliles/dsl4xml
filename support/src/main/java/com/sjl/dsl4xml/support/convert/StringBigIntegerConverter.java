package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

import java.math.BigInteger;

public class StringBigIntegerConverter extends StringConverter<BigInteger> {

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return BigInteger.class.isAssignableFrom(aClass);
    }

    @Override
    public BigInteger convert(String aFrom) {
        return new BigInteger(aFrom);
    }
}
