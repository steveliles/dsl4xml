package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class FloatConverterTest {

	@Test
	public void canConvertFloats() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertTrue(_c.canConvertTo(Float.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertEquals(new Float(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertEquals(new Float(Integer.MAX_VALUE - 2), _c.convert("2147483645"));
	}
	
	@Test
	public void convertsSmallRealValues() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertEquals(new Float(1), _c.convert("1.0"));
	}
	
	@Test
	public void convertsLargeRealValues() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertEquals(new Float(65535.65535f), _c.convert("65535.65535"));
	}
	
	@Test
	public void convertsNegativeRealValues() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertEquals(new Float(-65535.65535f), _c.convert("-65535.65535"));
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		FloatStringConverter _c = new FloatStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		FloatStringConverter _c = new FloatStringConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
