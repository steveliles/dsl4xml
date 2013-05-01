package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class DoubleConverterTest {
	
	@Test
	public void canConvertDoubles() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertTrue(_c.canConvertTo(Double.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertEquals(new Double(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertEquals(new Double(Integer.MAX_VALUE + 2L), _c.convert("2147483649"));
	}
	
	@Test
	public void convertsSmallRealValues() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertEquals(new Double(1), _c.convert("1.0"));
	}
	
	@Test
	public void convertsLargeRealValues() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertEquals(new Double(2147483647.012345d), _c.convert("2147483647.012345"));
	}
	
	@Test
	public void convertsLargeNegativeRealValues() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertEquals(new Double(9999999999.9999d), _c.convert("9999999999.9999"));
	}
	
	@Test
	public void convertsNullToNull() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		DoubleConverter _c = new DoubleConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
