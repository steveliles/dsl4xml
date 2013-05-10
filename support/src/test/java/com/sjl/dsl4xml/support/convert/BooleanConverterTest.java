package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class BooleanConverterTest {

	@Test
	public void canConvertBooleans() {
		BooleanStringConverter _c = new BooleanStringConverter();
		Assert.assertTrue(_c.canConvertTo(Boolean.class));
	}
	
	@Test
	public void convertsBooleanTrue() {
		BooleanStringConverter _c = new BooleanStringConverter();
		Assert.assertEquals(Boolean.TRUE, _c.convert("true"));
	}
	
	@Test
	public void convertsBooleanTrueCaseInsensitively() {
		BooleanStringConverter _c = new BooleanStringConverter();
		Assert.assertEquals(Boolean.TRUE, _c.convert("TrUe"));
	}
	
	@Test
	public void convertsBooleanFalse() {
		BooleanStringConverter _c = new BooleanStringConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert("false"));
	}
	
	@Test
	public void convertsNullStringToFalse() {
		BooleanStringConverter _c = new BooleanStringConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToFalse() {
		BooleanStringConverter _c = new BooleanStringConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert(""));
	}
	
	@Test
	public void convertsAnyOtherStringToFalse() {
		BooleanStringConverter _c = new BooleanStringConverter();
		Assert.assertEquals(Boolean.FALSE, _c.convert("wooble"));
		Assert.assertEquals(Boolean.FALSE, _c.convert("42"));
		Assert.assertEquals(Boolean.FALSE, _c.convert(" true "));
	}
	
}
