package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class IntegerConverterTest {

	@Test
	public void canConvertIntegers() {
		IntegerConverter _c = new IntegerConverter();
		Assert.assertTrue(_c.canConvertTo(Integer.class));
	}
	
	@Test
	public void convertsSmallOrdinalValues() {
		IntegerConverter _c = new IntegerConverter();
		Assert.assertEquals(new Integer(1), _c.convert("1"));
	}
	
	@Test
	public void convertsLargeOrdinalValues() {
		IntegerConverter _c = new IntegerConverter();
		Assert.assertEquals(new Integer(Integer.MAX_VALUE), _c.convert("2147483647"));
	}
	
	@Test
	public void convertsLargeNegativeOrdinalValues() {
		IntegerConverter _c = new IntegerConverter();
		Assert.assertEquals(new Integer(Integer.MIN_VALUE), _c.convert("-2147483648"));
	}
	
	@Test
	public void throwsExceptionOnNumbersGreaterThanIntegerMaxValue() {
		IntegerConverter _c = new IntegerConverter();
		try {
			_c.convert("2147483648");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsExceptionOnNonNumericInput() {
		IntegerConverter _c = new IntegerConverter();
		try {
			_c.convert("hello");
			Assert.fail("Expected an exception");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
	
	@Test
	public void convertsNullToNull() {
		IntegerConverter _c = new IntegerConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		IntegerConverter _c = new IntegerConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
}
