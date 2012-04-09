package com.sjl.dsl4xml;

// TODO: make this a checked exception
public class XmlReadingException extends RuntimeException
{
    public XmlReadingException(String aMessage)
    {
        super(aMessage);
    }
    
    public XmlReadingException(Exception aCause)
    {
        super(aCause);
    }
}
