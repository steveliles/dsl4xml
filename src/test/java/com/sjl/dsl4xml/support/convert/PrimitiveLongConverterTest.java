package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveLongConverterTest {

	@Test
	public void canConvertPrimitiveLongs() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		Assert.assertTrue(_c.canConvertTo(Long.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		Assert.assertTrue(1 == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		Assert.assertTrue(Long.MAX_VALUE == _c.convert("9223372036854775807"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		Assert.assertTrue(Long.MIN_VALUE == _c.convert("-9223372036854775808"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanPrimitiveLongMaxValue() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		try {
			_c.convert("9223372036854775808");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		Assert.assertTrue(0L == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		PrimitiveLongConverter _c = new PrimitiveLongConverter();
		Assert.assertTrue(0L == _c.convert(""));
	}

}
