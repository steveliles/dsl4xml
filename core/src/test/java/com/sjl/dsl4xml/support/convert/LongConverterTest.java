package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class LongConverterTest {

	@Test
	public void canConvertLongs() {
		LongStringConverter _c = new LongStringConverter();
		Assert.assertTrue(_c.canConvertTo(Long.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		LongStringConverter _c = new LongStringConverter();
		Assert.assertEquals(new Long(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		LongStringConverter _c = new LongStringConverter();
		Assert.assertEquals(new Long(Long.MAX_VALUE), _c.convert("9223372036854775807"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		LongStringConverter _c = new LongStringConverter();
		Assert.assertEquals(new Long(Long.MIN_VALUE), _c.convert("-9223372036854775808"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanLongMaxValue() {
		LongStringConverter _c = new LongStringConverter();
		try {
			_c.convert("9223372036854775808");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		LongStringConverter _c = new LongStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		LongStringConverter _c = new LongStringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		LongStringConverter _c = new LongStringConverter();
		Assert.assertEquals(null, _c.convert(""));
	}

}
