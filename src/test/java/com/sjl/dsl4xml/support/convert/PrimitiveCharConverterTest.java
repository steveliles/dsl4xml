package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveCharConverterTest {

	@Test
	public void canConvertPrimitiveChars() {
		PrimitiveCharConverter _c = new PrimitiveCharConverter();
		Assert.assertTrue(_c.canConvertTo(Character.TYPE));
	}
	
	@Test
	public void convertsSinglePrimitiveChar() {
		PrimitiveCharConverter _c = new PrimitiveCharConverter();
		Assert.assertTrue('s' == _c.convert("s"));
	}
	
	@Test
	public void convertsFirstPrimitiveCharIfMultiple() {
		PrimitiveCharConverter _c = new PrimitiveCharConverter();
		Assert.assertTrue('s' == _c.convert("steve"));
	}
	
}
