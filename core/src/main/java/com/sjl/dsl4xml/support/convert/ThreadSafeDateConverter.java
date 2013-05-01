package com.sjl.dsl4xml.support.convert;

import java.text.*;
import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

/**
 * A thread-safe date converter - only use this if you know you
 * need thread safety or don't care too much about having the absolute
 * best performance. There is a small overhead associated with
 * using ThreadLocal storage of the DateFormat.
 * 
 * @author steve
 */
public class ThreadSafeDateConverter implements Converter<Date> {

	private ThreadLocal<DateFormat> dateFormat;
	
	public ThreadSafeDateConverter(final String aDateFormatPattern) {
		dateFormat = new ThreadLocal<DateFormat>() {
			protected DateFormat initialValue() {
				return new SimpleDateFormat(aDateFormatPattern);
			}
		};
	}

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Date.class);
	}

	@Override
	public Date convert(String aValue) {
		try {
			return ((aValue == null) || ("".equals(aValue))) ? null : dateFormat.get().parse(aValue);
		} catch (ParseException anExc) {
			throw new ParsingException(anExc);
		}
	}
}
