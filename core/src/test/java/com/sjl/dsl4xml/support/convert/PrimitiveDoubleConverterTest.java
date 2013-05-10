package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveDoubleConverterTest {
	
	@Test
	public void canConvertPrimitiveDoubles() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue(_c.canConvertTo(Double.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue(1d == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue((double)Integer.MAX_VALUE + 2L == _c.convert("2147483649"));
	}
	
	@Test
	public void convertsSmallRealValues() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue(1d == _c.convert("1.0"));
	}
	
	@Test
	public void convertsLargeRealValues() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue(2147483647.012345d == _c.convert("2147483647.012345"));
	}
	
	@Test
	public void convertsLargeNegativeRealValues() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue(9999999999.9999d == _c.convert("9999999999.9999"));
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue(0d == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveDoubleStringConverter _c = new PrimitiveDoubleStringConverter();
		Assert.assertTrue(0d == _c.convert(""));
	}
	
}
