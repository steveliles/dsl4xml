package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveBooleanConverterTest {

	@Test
	public void canConvertPrimitiveBooleans() {
		PrimitiveBooleanConverter _c = new PrimitiveBooleanConverter();
		Assert.assertTrue(_c.canConvertTo(Boolean.TYPE));
	}
	
	@Test
	public void convertsPrimitiveBooleanTrue() {
		PrimitiveBooleanConverter _c = new PrimitiveBooleanConverter();
		Assert.assertEquals(true, _c.convert("true"));
	}
	
	@Test
	public void convertsPrimitiveBooleanTrueCaseInsensitively() {
		PrimitiveBooleanConverter _c = new PrimitiveBooleanConverter();
		Assert.assertEquals(true, _c.convert("TrUe"));
	}
	
	@Test
	public void convertsPrimitiveBooleanFalse() {
		PrimitiveBooleanConverter _c = new PrimitiveBooleanConverter();
		Assert.assertEquals(false, _c.convert("false"));
	}
	
	@Test
	public void convertsNullStringToFalse() {
		PrimitiveBooleanConverter _c = new PrimitiveBooleanConverter();
		Assert.assertEquals(false, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToFalse() {
		PrimitiveBooleanConverter _c = new PrimitiveBooleanConverter();
		Assert.assertEquals(false, _c.convert(""));
	}
	
	@Test
	public void convertsAnyOtherStringToFalse() {
		PrimitiveBooleanConverter _c = new PrimitiveBooleanConverter();
		Assert.assertEquals(false, _c.convert("wooble"));
		Assert.assertEquals(false, _c.convert("42"));
		Assert.assertEquals(false, _c.convert(" true "));
	}
	
}
