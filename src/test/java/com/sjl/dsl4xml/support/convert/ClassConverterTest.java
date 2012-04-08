package com.sjl.dsl4xml.support.convert;

import org.junit.*;

import com.sjl.dsl4xml.*;

public class ClassConverterTest {

	@Test
	public void canConvertClassNamesToClasses() {
		ClassConverter _c = new ClassConverter();
		Assert.assertEquals(
			ClassConverterTest.class, 
			_c.convert(ClassConverterTest.class.getCanonicalName())
		);
	}
	
	@Test
	public void throwsAppropriateExceptionIfClassNotFound() {
		ClassConverter _c = new ClassConverter();
		try {
			_c.convert("no.such.Class");
			Assert.fail("Expected an exception");
		} catch (XmlMarshallingException anExc) {
			Assert.assertEquals(ClassNotFoundException.class, anExc.getCause().getClass());
		}
	}
	
}
