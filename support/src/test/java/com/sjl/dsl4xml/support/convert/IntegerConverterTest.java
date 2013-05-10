package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class IntegerConverterTest {

	@Test
	public void canConvertIntegers() {
		IntegerStringConverter _c = new IntegerStringConverter();
		Assert.assertTrue(_c.canConvertTo(Integer.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		IntegerStringConverter _c = new IntegerStringConverter();
		Assert.assertEquals(new Integer(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		IntegerStringConverter _c = new IntegerStringConverter();
		Assert.assertEquals(new Integer(Integer.MAX_VALUE), _c.convert("2147483647"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		IntegerStringConverter _c = new IntegerStringConverter();
		Assert.assertEquals(new Integer(Integer.MIN_VALUE), _c.convert("-2147483648"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanIntegerMaxValue() {
		IntegerStringConverter _c = new IntegerStringConverter();
		try {
			_c.convert("2147483648");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		IntegerStringConverter _c = new IntegerStringConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		IntegerStringConverter _c = new IntegerStringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		IntegerStringConverter _c = new IntegerStringConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
