package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveFloatConverterTest {

	@Test
	public void canConvertPrimitiveFloats() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue(_c.canConvertTo(Float.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue(1f == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue((float)(Integer.MAX_VALUE - 2) == _c.convert("2147483645"));
	}
	
	@Test
	public void convertsSmallRealValues() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue(1f == _c.convert("1.0"));
	}
	
	@Test
	public void convertsLargeRealValues() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue(65535.65535f == _c.convert("65535.65535"));
	}
	
	@Test
	public void convertsNegativeRealValues() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue(-65535.65535f == _c.convert("-65535.65535"));
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue(0f == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveFloatStringConverter _c = new PrimitiveFloatStringConverter();
		Assert.assertTrue(0f == _c.convert(""));
	}
	
}
