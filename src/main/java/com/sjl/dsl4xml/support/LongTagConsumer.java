package com.sjl.dsl4xml.support;

import com.sjl.dsl4xml.XmlParseException;

public abstract class LongTagConsumer extends TextTagConsumer
{
    protected final void consume(String aTagName, String aText) throws XmlParseException
    {
        consume(aTagName, Long.parseLong(aText));
    }
    
    protected abstract void consume(String aTagName, long aValue) throws XmlParseException;
}
