package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.TypeSafeConverter;

import java.math.BigDecimal;

public class NumberBigDecimalConverter implements TypeSafeConverter<Number,BigDecimal> {

    @Override
    public boolean canConvertFrom(Class<?> aClass) {
        return Number.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canConvertTo(Class<?> aClass) {
        return aClass.isAssignableFrom(BigDecimal.class);
    }

    // only have to convert javascript types, so double
    // should be enough
    @Override
    public BigDecimal convert(Number aFrom) {
        return BigDecimal.valueOf(aFrom.doubleValue());
    }
}
