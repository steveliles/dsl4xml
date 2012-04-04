package com.sjl.dsl4xml.support;

import org.xmlpull.v1.XmlPullParser;

import com.sjl.dsl4xml.PullParser.TagConsumer;
import com.sjl.dsl4xml.XmlParseException;

import static com.sjl.dsl4xml.PullParser.*;

public abstract class TextTagConsumer implements TagConsumer
{
    public final void consume(String aTagName, XmlPullParser aParser) throws XmlParseException
    {
        String _text = getTextContent(aParser);
        if ("".equals(_text))
        {
            whenEmpty(aTagName);
        }
        else
        {
            consume(aTagName, _text);
        }
    }
    
    /**
     * Override this method if you want to do something when the tag is empty of any text.
     * 
     * @param aTagName
     * @throws XmlParseException
     */
    protected void whenEmpty(String aTagName) throws XmlParseException
    {            
    }
    
    /**
     * Invoked with the String value of the tag content IFF there is any content.
     * If you want to do something when there is no text content, override whenEmpty also.
     * 
     * @param aTagName
     * @param aText
     * @throws XmlParseException
     */
    protected abstract void consume(String aTagName, String aText) throws XmlParseException;        
}