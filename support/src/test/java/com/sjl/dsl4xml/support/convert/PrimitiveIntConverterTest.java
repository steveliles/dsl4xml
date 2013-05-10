package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveIntConverterTest {

	@Test
	public void canConvertPrimitiveInts() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		Assert.assertTrue(_c.canConvertTo(Integer.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		Assert.assertTrue(1 == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		Assert.assertTrue(Integer.MAX_VALUE == _c.convert("2147483647"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		Assert.assertTrue(Integer.MIN_VALUE == _c.convert("-2147483648"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanPrimitiveIntMaxValue() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		try {
			_c.convert("2147483648");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		Assert.assertTrue(0 == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveIntStringConverter _c = new PrimitiveIntStringConverter();
		Assert.assertTrue(0 == _c.convert(""));
	}
	
}
