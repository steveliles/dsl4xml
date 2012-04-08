package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class LongConverterTest {

	@Test
	public void canConvertLongs() {
		LongConverter _c = new LongConverter();
		Assert.assertTrue(_c.canConvertTo(Long.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		LongConverter _c = new LongConverter();
		Assert.assertEquals(new Long(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		LongConverter _c = new LongConverter();
		Assert.assertEquals(new Long(Long.MAX_VALUE), _c.convert("9223372036854775807"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		LongConverter _c = new LongConverter();
		Assert.assertEquals(new Long(Long.MIN_VALUE), _c.convert("-9223372036854775808"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanLongMaxValue() {
		LongConverter _c = new LongConverter();
		try {
			_c.convert("9223372036854775808");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		LongConverter _c = new LongConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		LongConverter _c = new LongConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		LongConverter _c = new LongConverter();
		Assert.assertEquals(null, _c.convert(""));
	}

}
