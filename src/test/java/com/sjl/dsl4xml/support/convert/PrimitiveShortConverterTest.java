package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveShortConverterTest {

	@Test
	public void canConvertPrimitiveShorts() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		Assert.assertTrue(_c.canConvertTo(Short.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		Assert.assertTrue((short)1 == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		Assert.assertTrue(Short.MAX_VALUE == _c.convert("32767"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		Assert.assertTrue(Short.MIN_VALUE == _c.convert("-32768"));
	}
	
	@Test
	public void throwsExceptionIfInputGreaterThanShortMaxValue() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		try {
			_c.convert("32768");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		Assert.assertTrue((short)0 == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveShortConverter _c = new PrimitiveShortConverter();
		Assert.assertTrue((short)0 == _c.convert(""));
	}
	
}
