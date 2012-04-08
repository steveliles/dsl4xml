package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveIntConverterTest {

	@Test
	public void canConvertPrimitiveInts() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		Assert.assertTrue(_c.canConvertTo(Integer.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		Assert.assertTrue(1 == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		Assert.assertTrue(Integer.MAX_VALUE == _c.convert("2147483647"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		Assert.assertTrue(Integer.MIN_VALUE == _c.convert("-2147483648"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanPrimitiveIntMaxValue() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		try {
			_c.convert("2147483648");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		Assert.assertTrue(0 == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveIntConverter _c = new PrimitiveIntConverter();
		Assert.assertTrue(0 == _c.convert(""));
	}
	
}
