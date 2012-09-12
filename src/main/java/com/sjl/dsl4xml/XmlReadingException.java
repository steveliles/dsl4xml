package com.sjl.dsl4xml;

public class XmlReadingException extends RuntimeException
{
    public XmlReadingException(String aMessage) {
        super(aMessage);
    }
    
    public XmlReadingException(Exception aCause) {
        super(aCause);
    }
    
    public XmlReadingException(String aMessage, Exception aCause) {
    	super (aMessage, aCause);
    }
}
