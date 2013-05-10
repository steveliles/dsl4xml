package com.sjl.dsl4xml;

public class ParsingException extends RuntimeException
{
    public ParsingException(String aMessage) {
        super(aMessage);
    }
    
    public ParsingException(Exception aCause) {
        super(aCause);
    }
    
    public ParsingException(String aMessage, Exception aCause) {
    	super (aMessage, aCause);
    }
}
