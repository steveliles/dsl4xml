package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveCharConverterTest {

	@Test
	public void canConvertPrimitiveChars() {
		PrimitiveCharStringConverter _c = new PrimitiveCharStringConverter();
		Assert.assertTrue(_c.canConvertTo(Character.TYPE));
	}
	
	@Test
	public void convertsSinglePrimitiveChar() {
		PrimitiveCharStringConverter _c = new PrimitiveCharStringConverter();
		Assert.assertTrue('s' == _c.convert("s"));
	}
	
	@Test
	public void convertsFirstPrimitiveCharIfMultiple() {
		PrimitiveCharStringConverter _c = new PrimitiveCharStringConverter();
		Assert.assertTrue('s' == _c.convert("steve"));
	}
	
}
