package com.sjl.dsl4xml;

// TODO: make this a checked exception
public class XmlParseException extends RuntimeException
{
    public XmlParseException(String aMessage)
    {
        super(aMessage);
    }
    
    public XmlParseException(Exception aCause)
    {
        super(aCause);
    }
}
