package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class FloatConverterTest {

	@Test
	public void canConvertFloats() {
		FloatConverter _c = new FloatConverter();
		Assert.assertTrue(_c.canConvertTo(Float.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		FloatConverter _c = new FloatConverter();
		Assert.assertEquals(new Float(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		FloatConverter _c = new FloatConverter();
		Assert.assertEquals(new Float(Integer.MAX_VALUE - 2), _c.convert("2147483645"));
	}
	
	@Test
	public void convertsSmallRealValues() {
		FloatConverter _c = new FloatConverter();
		Assert.assertEquals(new Float(1), _c.convert("1.0"));
	}
	
	@Test
	public void convertsLargeRealValues() {
		FloatConverter _c = new FloatConverter();
		Assert.assertEquals(new Float("65535.65535"), _c.convert("65535.65535"));
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		FloatConverter _c = new FloatConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		FloatConverter _c = new FloatConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		FloatConverter _c = new FloatConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
