package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class StringConverterTest {

	@Test
	public void canConvertStrings() {
		StringStringConverter _c = new StringStringConverter();
		Assert.assertTrue(_c.canConvertTo(String.class));
	}
	
	@Test
	public void convertsTextToString() {
		StringStringConverter _c = new StringStringConverter();
		Assert.assertEquals("hello there", _c.convert("hello there"));
	}
	
	@Test
	public void convertsNullToNull() {
		StringStringConverter _c = new StringStringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToEmptyString() {
		StringStringConverter _c = new StringStringConverter();
		Assert.assertEquals("", _c.convert(""));
	}
	
}
