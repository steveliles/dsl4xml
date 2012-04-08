package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class ShortConverterTest {

	@Test
	public void canConvertShorts() {
		ShortConverter _c = new ShortConverter();
		Assert.assertTrue(_c.canConvertTo(Short.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		ShortConverter _c = new ShortConverter();
		Assert.assertEquals(new Short((short)1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		ShortConverter _c = new ShortConverter();
		Assert.assertEquals(new Short(Short.MAX_VALUE), _c.convert("32767"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		ShortConverter _c = new ShortConverter();
		Assert.assertEquals(new Short(Short.MIN_VALUE),_c.convert("-32768"));
	}
	
	@Test
	public void throwsExceptionIfInputGreaterThanShortMaxValue() {
		ShortConverter _c = new ShortConverter();
		try {
			_c.convert("32768");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		ShortConverter _c = new ShortConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		ShortConverter _c = new ShortConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		ShortConverter _c = new ShortConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
