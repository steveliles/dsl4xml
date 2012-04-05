package com.sjl.dsl4xml;

// TODO: make this a checked exception
public class XmlMarshallingException extends RuntimeException
{
    public XmlMarshallingException(String aMessage)
    {
        super(aMessage);
    }
    
    public XmlMarshallingException(Exception aCause)
    {
        super(aCause);
    }
}
