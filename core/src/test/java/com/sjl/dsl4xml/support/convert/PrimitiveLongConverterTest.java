package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveLongConverterTest {

	@Test
	public void canConvertPrimitiveLongs() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		Assert.assertTrue(_c.canConvertTo(Long.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		Assert.assertTrue(1 == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		Assert.assertTrue(Long.MAX_VALUE == _c.convert("9223372036854775807"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		Assert.assertTrue(Long.MIN_VALUE == _c.convert("-9223372036854775808"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanPrimitiveLongMaxValue() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		try {
			_c.convert("9223372036854775808");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		Assert.assertTrue(0L == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		PrimitiveLongStringConverter _c = new PrimitiveLongStringConverter();
		Assert.assertTrue(0L == _c.convert(""));
	}

}
