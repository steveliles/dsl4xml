package com.sjl.dsl4xml.support.convert;

import org.junit.*;

import com.sjl.dsl4xml.*;

public class ClassConverterTest {

	@Test
	public void canConvertClassNamesToClasses() {
		ClassStringConverter _c = new ClassStringConverter();
		Assert.assertEquals(
			ClassConverterTest.class, 
			_c.convert(ClassConverterTest.class.getCanonicalName())
		);
	}
	
	@Test
	public void throwsAppropriateExceptionIfClassNotFound() {
		ClassStringConverter _c = new ClassStringConverter();
		try {
			_c.convert("no.such.Class");
			Assert.fail("Expected an exception");
		} catch (ParsingException anExc) {
			Assert.assertEquals(ClassNotFoundException.class, anExc.getCause().getClass());
		}
	}
	
}
