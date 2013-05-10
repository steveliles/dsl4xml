package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class DoubleConverterTest {
	
	@Test
	public void canConvertDoubles() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertTrue(_c.canConvertTo(Double.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertEquals(new Double(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertEquals(new Double(Integer.MAX_VALUE + 2L), _c.convert("2147483649"));
	}
	
	@Test
	public void convertsSmallRealValues() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertEquals(new Double(1), _c.convert("1.0"));
	}
	
	@Test
	public void convertsLargeRealValues() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertEquals(new Double(2147483647.012345d), _c.convert("2147483647.012345"));
	}
	
	@Test
	public void convertsLargeNegativeRealValues() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertEquals(new Double(9999999999.9999d), _c.convert("9999999999.9999"));
	}
	
	@Test
	public void convertsNullToNull() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		DoubleStringConverter _c = new DoubleStringConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
