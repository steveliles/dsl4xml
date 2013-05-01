package com.sjl.dsl4xml.sax;

import com.sjl.dsl4xml.*;

public class InvalidStateException extends ParsingException
{
	public InvalidStateException(String aMessage) {
		super(aMessage);
	}
}
