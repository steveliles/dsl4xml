package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveFloatConverterTest {

	@Test
	public void canConvertPrimitiveFloats() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue(_c.canConvertTo(Float.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue(1f == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue((float)(Integer.MAX_VALUE - 2) == _c.convert("2147483645"));
	}
	
	@Test
	public void convertsSmallRealValues() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue(1f == _c.convert("1.0"));
	}
	
	@Test
	public void convertsLargeRealValues() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue(65535.65535f == _c.convert("65535.65535"));
	}
	
	@Test
	public void convertsNegativeRealValues() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue(-65535.65535f == _c.convert("-65535.65535"));
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue(0f == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveFloatConverter _c = new PrimitiveFloatConverter();
		Assert.assertTrue(0f == _c.convert(""));
	}
	
}
