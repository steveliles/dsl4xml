package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveBooleanConverterTest {

	@Test
	public void canConvertPrimitiveBooleans() {
		PrimitiveBooleanStringConverter _c = new PrimitiveBooleanStringConverter();
		Assert.assertTrue(_c.canConvertTo(Boolean.TYPE));
	}
	
	@Test
	public void convertsPrimitiveBooleanTrue() {
		PrimitiveBooleanStringConverter _c = new PrimitiveBooleanStringConverter();
		Assert.assertEquals(true, _c.convert("true"));
	}
	
	@Test
	public void convertsPrimitiveBooleanTrueCaseInsensitively() {
		PrimitiveBooleanStringConverter _c = new PrimitiveBooleanStringConverter();
		Assert.assertEquals(true, _c.convert("TrUe"));
	}
	
	@Test
	public void convertsPrimitiveBooleanFalse() {
		PrimitiveBooleanStringConverter _c = new PrimitiveBooleanStringConverter();
		Assert.assertEquals(false, _c.convert("false"));
	}
	
	@Test
	public void convertsNullStringToFalse() {
		PrimitiveBooleanStringConverter _c = new PrimitiveBooleanStringConverter();
		Assert.assertEquals(false, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToFalse() {
		PrimitiveBooleanStringConverter _c = new PrimitiveBooleanStringConverter();
		Assert.assertEquals(false, _c.convert(""));
	}
	
	@Test
	public void convertsAnyOtherStringToFalse() {
		PrimitiveBooleanStringConverter _c = new PrimitiveBooleanStringConverter();
		Assert.assertEquals(false, _c.convert("wooble"));
		Assert.assertEquals(false, _c.convert("42"));
		Assert.assertEquals(false, _c.convert(" true "));
	}
	
}
