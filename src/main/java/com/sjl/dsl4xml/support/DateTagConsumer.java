package com.sjl.dsl4xml.support;

import java.text.DateFormat;
import java.util.Date;

import com.sjl.dsl4xml.XmlParseException;

public abstract class DateTagConsumer extends TextTagConsumer
{
    private DateFormat format;
    
    public DateTagConsumer(DateFormat aFormat)
    {
        format = aFormat;
    }
    
    protected final void consume(String aTagName, String aText) throws XmlParseException
    {
        try
        {
            consume(aTagName, format.parse(aText));
        }
        catch (Exception anExc)
        {
            throw new XmlParseException(anExc);
        }
    }
    
    protected abstract void consume(String aTagName, Date aDate) throws XmlParseException;
}
