package com.sjl.dsl4xml.support.convert;

import java.text.*;
import java.util.*;

import org.junit.*;

import com.sjl.dsl4xml.*;

public class ThreadSafeDateConverterTest {

	@Test
	public void testCanConvertDates() {
		ThreadSafeDateConverter _c = new ThreadSafeDateConverter("yyyyMMdd");
		Assert.assertTrue(_c.canConvertTo(Date.class));
	}
	
	@Test
	public void testConvertsDatesUsingDateFormatPattern() throws Exception {
		ThreadSafeDateConverter _c = new ThreadSafeDateConverter("yyyyMMdd");
		DateFormat _df = new SimpleDateFormat("yyyyMMdd");
		Assert.assertEquals(_df.parse("20120408"), _c.convert("20120408"));
	}
	
	@Test
	public void testConvertsDatesAndTimesUsingDateFormatPattern() throws Exception {
		ThreadSafeDateConverter _c = new ThreadSafeDateConverter("yyyyMMddHHmmss");
		DateFormat _df = new SimpleDateFormat("yyyyMMddHHmmss");
		Assert.assertEquals(_df.parse("20120408172033"), _c.convert("20120408172033"));
	}
	
	@Test
	public void testThrowsExceptionWhenInvalidDateFormatPattern() throws Exception {
		ThreadSafeDateConverter _c = new ThreadSafeDateConverter("fnar");
		try {
			_c.convert("20120408");
			Assert.fail("Expected an exception");
		} catch (IllegalArgumentException anExc) {
			// good
		}
	}
	
	@Test
	public void testThrowsExceptionWhenInvalidDateValue() throws Exception {
		ThreadSafeDateConverter _c = new ThreadSafeDateConverter("20120408");
		try {
			_c.convert("boo");
			Assert.fail("Expected an exception");
		} catch (XmlReadingException anExc) {
			Assert.assertEquals(ParseException.class, anExc.getCause().getClass());
		}
	}
	
	@Test
	public void testReturnsNullWhenEmptyString() {
		ThreadSafeDateConverter _c = new ThreadSafeDateConverter("yyyyMMdd");
		Assert.assertEquals(null, _c.convert(""));
	}
	
	@Test
	public void testReturnsNullWhenNullValue() {
		ThreadSafeDateConverter _c = new ThreadSafeDateConverter("yyyyMMdd");
		Assert.assertEquals(null, _c.convert(null));
	}
	
}
