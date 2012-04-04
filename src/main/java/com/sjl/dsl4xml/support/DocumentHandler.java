package com.sjl.dsl4xml.support;

import org.xmlpull.v1.XmlPullParser;

import com.sjl.dsl4xml.PullParser.Handler;
import com.sjl.dsl4xml.XmlParseException;

public class DocumentHandler implements Handler
{
    private Handler[] handlers;
    
    public DocumentHandler(Handler... aHandlers)
    {
        handlers = aHandlers;
    }
    
    @Override
    public boolean handle(XmlPullParser aParser) 
    throws XmlParseException
    {
        try
        {
            while (aParser.getEventType() != XmlPullParser.END_DOCUMENT)
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
            return true;
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
