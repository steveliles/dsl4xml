package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveByteConverterTest {

	@Test
	public void canConvertStringsToPrimitiveByte() {
		PrimitiveByteConverter _c = new PrimitiveByteConverter();
		Assert.assertTrue(_c.canConvertTo(Byte.TYPE));
	}
	
	@Test
	public void canConvertPrimitiveByteValues() {
		PrimitiveByteConverter _c = new PrimitiveByteConverter();
		Assert.assertTrue((byte)23 == _c.convert("23"));
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveByteConverter _c = new PrimitiveByteConverter();
		Assert.assertTrue((byte)0 == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveByteConverter _c = new PrimitiveByteConverter();
		Assert.assertTrue((byte)0 == _c.convert(""));
	}
	
	@Test
	public void throwsExceptionForNonNullNonNumericValues() {
		PrimitiveByteConverter _c = new PrimitiveByteConverter();
		try {
			_c.convert("fred");
			Assert.fail("Expected an exception!");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
}
