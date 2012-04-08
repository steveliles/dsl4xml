package com.sjl.dsl4xml.support.convert;

import java.text.*;
import java.util.*;

import org.junit.*;

import com.sjl.dsl4xml.*;

public class ThreadUnsafeDateConverterTest {

	@Test
	public void testCanConvertDates() {
		ThreadUnsafeDateConverter _c = new ThreadUnsafeDateConverter("yyyyMMdd");
		Assert.assertTrue(_c.canConvertTo(Date.class));
	}
	
	@Test
	public void testConvertsDatesUsingDateFormatPattern() throws Exception {
		ThreadUnsafeDateConverter _c = new ThreadUnsafeDateConverter("yyyyMMdd");
		DateFormat _df = new SimpleDateFormat("yyyyMMdd");
		Assert.assertEquals(_df.parse("20120408"), _c.convert("20120408"));
	}
	
	@Test
	public void testConvertsDatesAndTimesUsingDateFormatPattern() throws Exception {
		ThreadUnsafeDateConverter _c = new ThreadUnsafeDateConverter("yyyyMMddHHmmss");
		DateFormat _df = new SimpleDateFormat("yyyyMMddHHmmss");
		Assert.assertEquals(_df.parse("20120408172033"), _c.convert("20120408172033"));
	}
	
	@Test
	public void testThrowsExceptionWhenInvalidDateFormatPattern() throws Exception {
		try {
			ThreadUnsafeDateConverter _c = new ThreadUnsafeDateConverter("fnar");
			Assert.fail("Expected an exception");
		} catch (IllegalArgumentException anExc) {
			// good
		}
	}
	
	@Test
	public void testThrowsExceptionWhenInvalidDateValue() throws Exception {
		ThreadUnsafeDateConverter _c = new ThreadUnsafeDateConverter("20120408");
		try {
			_c.convert("boo");
			Assert.fail("Expected an exception");
		} catch (XmlMarshallingException anExc) {
			Assert.assertEquals(ParseException.class, anExc.getCause().getClass());
		}
	}

	@Test
	public void testReturnsNullWhenEmptyString() {
		ThreadUnsafeDateConverter _c = new ThreadUnsafeDateConverter("yyyyMMdd");
		Assert.assertEquals(null, _c.convert(""));
	}
	
	@Test
	public void testReturnsNullWhenNullValue() {
		ThreadUnsafeDateConverter _c = new ThreadUnsafeDateConverter("yyyyMMdd");
		Assert.assertEquals(null, _c.convert(null));
	}
}
