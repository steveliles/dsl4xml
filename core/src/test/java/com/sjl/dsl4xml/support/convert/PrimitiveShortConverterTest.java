package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveShortConverterTest {

	@Test
	public void canConvertPrimitiveShorts() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		Assert.assertTrue(_c.canConvertTo(Short.TYPE));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		Assert.assertTrue((short)1 == _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		Assert.assertTrue(Short.MAX_VALUE == _c.convert("32767"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		Assert.assertTrue(Short.MIN_VALUE == _c.convert("-32768"));
	}
	
	@Test
	public void throwsExceptionIfInputGreaterThanShortMaxValue() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		try {
			_c.convert("32768");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		Assert.assertTrue((short)0 == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveShortStringConverter _c = new PrimitiveShortStringConverter();
		Assert.assertTrue((short)0 == _c.convert(""));
	}
	
}
