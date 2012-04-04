package com.sjl.dsl4xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;

import com.sjl.dsl4xml.support.DocumentHandler;
import com.sjl.dsl4xml.support.TagContentHandler;
import com.sjl.dsl4xml.support.TagHandler;

public class PullParser
{
    public interface TagConsumer
    {
        public static TagConsumer NULL_OBJECT = new TagConsumer()
        {
            @Override
            public void consume(String aTagName, XmlPullParser aParser) 
            throws XmlParseException {}            
        };
        
        public void consume(String aTagName, XmlPullParser aParser)
        throws XmlParseException;
    }
    
    public interface Handler
    {
        public boolean handle(XmlPullParser aParser)
        throws XmlParseException;
    }
    
    public interface TagOpened
    {
        public static final TagOpened NULL_OBJECT = new TagOpened()
        {
            public void invoke(){}
        };
        
        public void invoke();
    }
    
    public interface TagClosed
    {
        public static final TagClosed NULL_OBJECT = new TagClosed()
        {
            public void invoke(){}
        };
        
        public void invoke();
    }
    
    /**
     * Factory method for creating document handlers with a set of lower level handlers.
     * Usage pattern:
     *   
     *   whileNotEndOfDocument(
     *       new Handler() { ... },
     *       new Handler() { ... },
     *       new Handler() { ... }
     *   )
     * 
     * @param aHandlers - the tag-level handlers for this document type
     * @return top-level handler that should be invoked by parsers
     */
    public static Handler whileNotEndOfDocument(Handler... aHandlers)
    {
        return new DocumentHandler(aHandlers);
    }
    
    public static TagConsumer withTagContent(Handler... aHandlers)
    {
        return new TagContentHandler(aHandlers);
    }
    
    /**
     * Factory method for creating tag-handlers for handling tags of a specific name.
     * Usage pattern: 
     * 
     *   whileNotEndOfDocument(
     *       whenTag("title").use(new TagConsumer() { ... }),
     *       whenTag("summary").use(new TagConsumer() { ... })
     *   );
     * 
     * @param aTagName
     * @return a handler for the given tag name
     */
    public static TagHandler whenTag(String aTagName)
    {
        return new TagHandler(aTagName);
    }
    
    
    public static String getAttributeAtIndex(XmlPullParser aParser, int anAttributeIndex, String aDefaultValue)
    throws XmlParseException
    {
        return (aParser.getAttributeCount() > anAttributeIndex) ?
            aParser.getAttributeValue(anAttributeIndex) : aDefaultValue;
    }
    
    /**
     * Convenience method for extracting text from simple tags containing only CDATA.
     * 
     * @param aParser
     * @return The text content of the current tag, if there is any, or empty String otherwise.
     * @throws XmlParseException
     */
    public static String getTextContent(XmlPullParser aParser)
    throws XmlParseException
    {
        try
        {
            return aParser.nextText();
        }
        catch (Exception anExc)
        {
            throw new XmlParseException(anExc);
        }
    }
    
    /**
     * Convenience method for extracting Long values from simple tags containing only CDATA
     * 
     * @param aParser
     * @return Long value of the CDATA content of the tag, if available, the default value otherwise
     * @throws XmlParseException
     */
    public static long getTextContentAsLong(XmlPullParser aParser)
    throws XmlParseException
    {
        try
        {
            String _text = getTextContent(aParser);       
            return ("".equals(_text)) ? null : Long.parseLong(_text);            
        }
        catch (NumberFormatException anExc)
        {
            throw new XmlParseException(anExc);
        }
    }
    
    /**
     * Convenience method for extracting Date values from simple tags containing only CDATA
     * 
     * @param aParser
     * @param aDateFormat
     * @return Date 
     * @throws XmlParseException
     */
    public static Date getTextContentAsDate(XmlPullParser aParser, DateFormat aDateFormat)
    throws XmlParseException
    {
        try
        {
            String _text = getTextContent(aParser);
            return ("".equals(_text)) ? null : aDateFormat.parse(_text);
        }
        catch (ParseException anExc)
        {
            throw new XmlParseException(anExc);
        }
    }
    
    /**
     * This method is purely to make the internal DSL read nicely when creating content handlers.
     * Example usage:
     * 
     *   whenTag("item").opened(
     *       first(initialiseNextItem()).
     *       thenUse(itemContentHandlers()).
     *       whenClosed(addFinalizedItemToItems())
     *   )
     * 
     * @param aTagOpened
     * @return aTagOpened - completely unchanged
     */
    public static TagOpened first(TagOpened aTagOpened)
    {
        return aTagOpened;
    }
    
    /**
     * Purely to make the internal DSL read nicely when creating content handlers.
     * Example usage:
     * 
     *   whenTag("item").opened(
           first(initialiseAnItem()),
           thenConsumeContentWith(
             whenTag("title")...
           )
         )
     * 
     * @param aHandlers
     * @return the same handler array as was passed in
     */
    public static Handler[] then(Handler... aHandlers)
    {
        return aHandlers;
    }
    
    /**
     * Purely to make the internal DSL read nicely when creating content handlers.
     * Example usage:
     * 
     *   whenTag("item").opened(
     *     first(initialiseAnItem()),
     *     thenConsumeContentWith(
     *       whenTag("title")...
     *     )
     *   )
     * 
     * @param aTagClosed
     * @return the same TagClosed as was passed in
     */
    public static TagClosed whenFinished(TagClosed aTagClosed)
    {
        return aTagClosed;
    }    
    
    private XmlPullParser delegate;    
    private Handler handler;
    
    public PullParser(XmlPullParser aDelegate, Handler aHandler)
    {
        delegate = aDelegate;
        handler = aHandler;
    }    
    
    public void parse() 
    throws XmlParseException
    {
        handler.handle(delegate);
    }
}
