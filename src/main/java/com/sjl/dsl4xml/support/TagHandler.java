package com.sjl.dsl4xml.support;

import org.xmlpull.v1.XmlPullParser;

import com.sjl.dsl4xml.PullParser.Handler;
import com.sjl.dsl4xml.PullParser.TagClosed;
import com.sjl.dsl4xml.PullParser.TagConsumer;
import com.sjl.dsl4xml.PullParser.TagOpened;
import com.sjl.dsl4xml.XmlParseException;

public class TagHandler implements Handler
{
    private String tagName;
    private TagConsumer callback = TagConsumer.NULL_OBJECT;
    
    public TagHandler(String aTagName)
    {
        tagName = aTagName;
    }
    
    public TagHandler opened(TagOpened aFirst, Handler[] aHandlers, TagClosed aFinalizer)
    {
        callback = new TagContentHandler(aFirst, aHandlers, aFinalizer);
        return this;
    }
    
    public TagHandler opened(TagOpened aFirst, Handler[] aHandlers)
    {        
        return opened(aFirst, aHandlers, TagClosed.NULL_OBJECT);
    }
    
    public TagHandler opened(Handler[] aHandlers, TagClosed aFinalizer)
    {        
        return opened(TagOpened.NULL_OBJECT, aHandlers, aFinalizer);
    }
    
    public TagHandler opened(Handler[] aHandlers)
    {        
        return opened(TagOpened.NULL_OBJECT, aHandlers, TagClosed.NULL_OBJECT);
    }
    
    public TagHandler use(TagConsumer aCallback)
    {           
        callback = aCallback;
        return this;
    }
    
    public TagHandler then(TagHandler... aNestedTagHandlers)
    {
        callback = new TagContentHandler(aNestedTagHandlers);
        return this;
    }
    
    @Override
    public boolean handle(XmlPullParser aParser) 
    throws XmlParseException
    {
        try
        {     
            if ((aParser.getEventType() == XmlPullParser.START_TAG) && 
                (tagName.equals(aParser.getName())))
            {
                callback.consume(tagName, aParser);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (XmlParseException anExc)
        {
            throw anExc;
        }
        catch (Exception anExc)
        {
            throw new XmlParseException(anExc);
        }
    }        
}
