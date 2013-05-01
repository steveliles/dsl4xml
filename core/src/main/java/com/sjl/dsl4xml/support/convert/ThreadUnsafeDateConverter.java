package com.sjl.dsl4xml.support.convert;

import java.text.*;
import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

/**
 * A Date converter that should only be used when you know that
 * the PullDocumentReader that refers to this converter will only
 * ever be used in a single-threaded manner.
 * 
 * For multi-threaded usage, use ThreadSafeDateConverter instead.
 * 
 * @author steve
 */
public class ThreadUnsafeDateConverter implements Converter<Date> {

	private DateFormat dateFormat;
	
	public ThreadUnsafeDateConverter(String aDateFormatPattern) {
		dateFormat = new SimpleDateFormat(aDateFormatPattern);
	}
	
	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Date.class);
	}

	@Override
	public Date convert(String aValue) {
		try {
			return ((aValue == null) || ("".equals(aValue))) ? null : dateFormat.parse(aValue);
		} catch (ParseException anExc) {
			throw new ParsingException(anExc);
		}
	}
	
}
