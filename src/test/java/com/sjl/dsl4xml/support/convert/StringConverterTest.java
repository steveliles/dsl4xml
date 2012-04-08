package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class StringConverterTest {

	@Test
	public void canConvertStrings() {
		StringConverter _c = new StringConverter();
		Assert.assertTrue(_c.canConvertTo(String.class));
	}
	
	@Test
	public void convertsTextToString() {
		StringConverter _c = new StringConverter();
		Assert.assertEquals("hello there", _c.convert("hello there"));
	}
	
	@Test
	public void convertsNullToNull() {
		StringConverter _c = new StringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToEmptyString() {
		StringConverter _c = new StringConverter();
		Assert.assertEquals("", _c.convert(""));
	}
	
}
