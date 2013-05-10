package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class ByteConverterTest {

	@Test
	public void canConvertStringsToByte() {
		ByteStringConverter _c = new ByteStringConverter();
		Assert.assertTrue(_c.canConvertTo(Byte.class));
	}
	
	@Test
	public void canConvertByteValues() {
		ByteStringConverter _c = new ByteStringConverter();
		Assert.assertEquals(new Byte((byte)23), _c.convert("23"));
	}
	
	@Test
	public void convertsNullToNull() {
		ByteStringConverter _c = new ByteStringConverter();
		Assert.assertEquals(null, _c.convert(null));
	}
	
	@Test
	public void convertsEmptyStringToNull() {
		ByteStringConverter _c = new ByteStringConverter();
		Assert.assertEquals(null, _c.convert(""));
	}
	
	@Test
	public void throwsExceptionForNonNullNonNumericValues() {
		ByteStringConverter _c = new ByteStringConverter();
		try {
			_c.convert("fred");
			Assert.fail("Expected an exception!");
		} catch (NumberFormatException anExc) {
			// good
		}
	}
}
