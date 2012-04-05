package com.sjl.dsl4xml;

import java.io.*;

import org.xmlpull.v1.*;

public class DocumentMapper<T> {

	public static <R> DocumentMapper<R> mappingOf(Class<R> aClass) {
		return new DocumentMapper<R>(aClass);
	}
	
	public static <R> TagMapper<R> tag(String aTagName) {
		return new TagMapper<R>(aTagName);
	}
	
	public static <R> TagMapper<R> tag(String aTagName, Class<R> aContextType) {
		return new TagMapper<R>(aTagName, aContextType);
	}
	
	public static AttributesMapper attributes(String... anAttributeNames) {
		return new AttributesMapper(anAttributeNames);
	}
	
	public static TagsMapper tags(String aTagName) {
		return new TagsMapper(aTagName);
	}

	private XmlPullParserFactory factory;
	private Mapper[] mappers;
	private Class<T> resultType;
	
	public DocumentMapper(Class<T> aClass) {
		resultType = aClass;
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException anExc) {
			throw new XmlParseException(anExc);
		}
	}

	public DocumentMapper<T> with(Mapper... aMappers) {
		mappers = aMappers;
		return this;
	}
	
	public T map(InputStream anInputStream, String aCharSet) {
		return map(newReader(anInputStream, aCharSet));
	}
	
	public T map(Reader aReader)
	throws XmlParseException {
		try {
			XmlPullParser _p = factory.newPullParser();
			_p.setInput(aReader);
		    MappingContext _ctx = new MappingContext(_p);
		    _ctx.push(resultType.newInstance());
		    
		    try
	        {
	            while (_ctx.hasMoreTags())
	            {                   
	                for (Mapper _m : mappers)
	                {                 
	                    if (_m.map(_ctx))
	                    {                            
	                        break;
	                    }
	                }
	                
	                if (_ctx.hasMoreTags())
	                	_ctx.next();
	            }
	            return _ctx.peek();
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


	private Reader newReader(InputStream anInputStream, String aCharSet) {
		try {
			return new InputStreamReader(anInputStream, aCharSet);
		} catch (UnsupportedEncodingException anExc) {
			throw new RuntimeException(anExc);
		}
	}
}
