package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.StringConverter;

import java.math.BigDecimal;

public class StringBigDecimalConverter extends StringConverter<BigDecimal> {

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return BigDecimal.class.isAssignableFrom(aClass);
    }

    @Override
    public BigDecimal convert(String aFrom) {
        return new BigDecimal(aFrom);
    }
}
