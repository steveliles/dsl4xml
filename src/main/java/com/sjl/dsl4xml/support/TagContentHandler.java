package com.sjl.dsl4xml.support;

import org.xmlpull.v1.XmlPullParser;

import com.sjl.dsl4xml.PullParser.Handler;
import com.sjl.dsl4xml.PullParser.TagClosed;
import com.sjl.dsl4xml.PullParser.TagConsumer;
import com.sjl.dsl4xml.PullParser.TagOpened;
import com.sjl.dsl4xml.XmlParseException;

public class TagContentHandler implements TagConsumer
{
    private TagOpened tagOpened = TagOpened.NULL_OBJECT;
    private Handler[] handlers = new Handler[]{};
    private TagClosed tagClosed = TagClosed.NULL_OBJECT;
    
    public TagContentHandler(TagOpened anOpened, Handler[] aHandlers, TagClosed aClosed)
    {
        tagOpened = anOpened;
        handlers = aHandlers;
        tagClosed = aClosed;
    }
    
    public TagContentHandler(Handler... aHandlers)
    {
        handlers = aHandlers;
    }
    
    @Override
    public void consume(String aTagName, XmlPullParser aParser) 
    throws XmlParseException
    {
        tagOpened.invoke();
        
        try
        {
            // step into the tag
            aParser.next();
            
            // NOTE: Does NOT currently allow for nested tags of the same name as
            //       the current tag!!
            while (notEndOfCurrentTag(aTagName, aParser)) 
            {
                for (Handler _h : handlers)
                {                 
                    if (_h.handle(aParser))
                    {                            
                        break;
                    }
                }                    
                aParser.next();                    
            }
            
            tagClosed.invoke();
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
    
    private boolean notEndOfCurrentTag(String aTagName, XmlPullParser aParser)
    throws Exception
    {
        return (!((aTagName.equals(aParser.getName()) && (aParser.getEventType() == XmlPullParser.END_TAG))));
    }
}