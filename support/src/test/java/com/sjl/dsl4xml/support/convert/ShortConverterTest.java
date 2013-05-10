package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class ShortConverterTest {

	@Test
	public void canConvertShorts() {
		ShortStringConverter _c = new ShortStringConverter();
		Assert.assertTrue(_c.canConvertTo(Short.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		ShortStringConverter _c = new ShortStringConverter();
		Assert.assertEquals(new Short((short)1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		ShortStringConverter _c = new ShortStringConverter();
		Assert.assertEquals(new Short(Short.MAX_VALUE), _c.convert("32767"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		ShortStringConverter _c = new ShortStringConverter();
		Assert.assertEquals(new Short(Short.MIN_VALUE),_c.convert("-32768"));
	}
	
	@Test
	public void throwsExceptionIfInputGreaterThanShortMaxValue() {
		ShortStringConverter _c = new ShortStringConverter();
		try {
			_c.convert("32768");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		ShortStringConverter _c = new ShortStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		ShortStringConverter _c = new ShortStringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		ShortStringConverter _c = new ShortStringConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
