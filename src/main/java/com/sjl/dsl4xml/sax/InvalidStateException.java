package com.sjl.dsl4xml.sax;

import com.sjl.dsl4xml.*;

public class InvalidStateException extends XmlReadingException {
	public InvalidStateException(String aMessage) {
		super(aMessage);
	}
}
