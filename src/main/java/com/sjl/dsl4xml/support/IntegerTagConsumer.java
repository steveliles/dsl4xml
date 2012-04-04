package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.XmlParseException;

public abstract class IntegerTagConsumer extends TextTagConsumer
{
    protected final void consume(String aTagName, String aText) throws XmlParseException
    {
        consume(aTagName, Integer.parseInt(aText));
    }
    
    protected abstract void consume(String aTagName, int aValue) throws XmlParseException;
}
