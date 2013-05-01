package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class ByteConverterTest {

	@Test
	public void canConvertStringsToByte() {
		ByteConverter _c = new ByteConverter();
		Assert.assertTrue(_c.canConvertTo(Byte.class));
	}
	
	@Test
	public void canConvertByteValues() {
		ByteConverter _c = new ByteConverter();
		Assert.assertEquals(new Byte((byte)23), _c.convert("23"));
	}
	
	@Test
	public void convertsNullToNull() {
		ByteConverter _c = new ByteConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		ByteConverter _c = new ByteConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
	@Test
	public void throwsExceptionForNonNullNonNumericValues() {
		ByteConverter _c = new ByteConverter();
		try {
			_c.convert("fred");
			Assert.fail("Expected an exception!");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
}
