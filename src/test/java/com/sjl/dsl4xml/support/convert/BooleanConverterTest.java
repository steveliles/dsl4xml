package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class BooleanConverterTest {

	@Test
	public void canConvertBooleans() {
		BooleanConverter _c = new BooleanConverter();
		Assert.assertTrue(_c.canConvertTo(Boolean.class));
	}
	
	@Test
	public void convertsBooleanTrue() {
		BooleanConverter _c = new BooleanConverter();
		Assert.assertEquals(Boolean.TRUE, _c.convert("true"));
	}
	
	@Test
	public void convertsBooleanTrueCaseInsensitively() {
		BooleanConverter _c = new BooleanConverter();
		Assert.assertEquals(Boolean.TRUE, _c.convert("TrUe"));
	}
	
	@Test
	public void convertsBooleanFalse() {
		BooleanConverter _c = new BooleanConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert("false"));
	}
	
	@Test
	public void convertsNullStringToFalse() {
		BooleanConverter _c = new BooleanConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToFalse() {
		BooleanConverter _c = new BooleanConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert(""));
	}
	
	@Test
	public void convertsAnyOtherStringToFalse() {
		BooleanConverter _c = new BooleanConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert("wooble"));
		Assert.assertEquals(Boolean.FALSE, _c.convert("42"));
		Assert.assertEquals(Boolean.FALSE, _c.convert(" true "));
	}
	
}
