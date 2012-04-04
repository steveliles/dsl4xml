package com.sjl.dsl4xml;

import java.io.*;

import org.xmlpull.v1.*;

public class DocumentMapper<T> {

	private XmlPullParserFactory factory;
	private Mapper[] mappers;
	
	public static <R> DocumentMapper<R> xmlMappingTo(Class<R> aClass) {
		return new DocumentMapper<R>();
	}
	
	public static TagMapper tag(String aTagName) {
		return new TagMapper(aTagName);
	}
	
	public static TagsMapper tags(String aTagName) {
		return new TagsMapper(aTagName);
	}
	
	public DocumentMapper<T> with(Mapper... aMappers) {
		mappers = aMappers;
		return this;
	}
	
	public DocumentMapper() {
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException anExc) {
			throw new XmlParseException(anExc);
		}
	}
	
	public boolean map(Reader aReader, MappingContext aContext)
	throws XmlParseException {
		try {
			XmlPullParser _p = factory.newPullParser();
		    _p.setInput(aReader);
		    aContext.setParser(_p);
		    
		    try
	        {
	            while (aContext.hasMoreTags())
	            {                   
	                for (Mapper _m : mappers)
	                {                 
	                    if (_m.map(aContext))
	                    {                            
	                        break;
	                    }
	                }
	                
	                if (aContext.hasMoreTags())
	                	aContext.next();
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
		} catch (XmlParseException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new XmlParseException(anExc);
		}
	}
	
}
