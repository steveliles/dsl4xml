package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class PrimitiveByteConverterTest {

	@Test
	public void canConvertStringsToPrimitiveByte() {
		PrimitiveByteStringConverter _c = new PrimitiveByteStringConverter();
		Assert.assertTrue(_c.canConvertTo(Byte.TYPE));
	}
	
	@Test
	public void canConvertPrimitiveByteValues() {
		PrimitiveByteStringConverter _c = new PrimitiveByteStringConverter();
		Assert.assertTrue((byte)23 == _c.convert("23"));
	}
	
	@Test
	public void convertsNullToZero() {
		PrimitiveByteStringConverter _c = new PrimitiveByteStringConverter();
		Assert.assertTrue((byte)0 == _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToZero() {
		PrimitiveByteStringConverter _c = new PrimitiveByteStringConverter();
		Assert.assertTrue((byte)0 == _c.convert(""));
	}
	
	@Test
	public void throwsExceptionForNonNullNonNumericValues() {
		PrimitiveByteStringConverter _c = new PrimitiveByteStringConverter();
		try {
			_c.convert("fred");
			Assert.fail("Expected an exception!");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
}
