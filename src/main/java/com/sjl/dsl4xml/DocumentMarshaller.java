package com.sjl.dsl4xml;

import java.io.*;

import org.xmlpull.v1.*;

public class DocumentMarshaller<T> {

	public static <R> DocumentMarshaller<R> mappingOf(Class<R> aClass) {
		return new DocumentMarshaller<R>(aClass);
	}
	
	public static <R> TagMarshaller<R> tag(String aTagName) {
		return new TagMarshaller<R>(aTagName);
	}
	
	public static <R> TagMarshaller<R> tag(String aTagName, Class<R> aContextType) {
		return new TagMarshaller<R>(aTagName, aContextType);
	}
	
	public static AttributesMarshaller attributes(String... anAttributeNames) {
		return new AttributesMarshaller(anAttributeNames);
	}
	
	private XmlPullParserFactory factory;
	private Marshaller[] mappers;
	private Class<T> resultType;
	
	public DocumentMarshaller(Class<T> aClass) {
		resultType = aClass;
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}

	public DocumentMarshaller<T> to(Marshaller... aMappers) {
		mappers = aMappers;
		return this;
	}
	
	public T map(InputStream anInputStream, String aCharSet) {
		return map(newReader(anInputStream, aCharSet));
	}
	
	public T map(Reader aReader)
	throws XmlMarshallingException {
		try {
			XmlPullParser _p = factory.newPullParser();
			_p.setInput(aReader);
		    MarshallingContext _ctx = new MarshallingContext(_p);
		    _ctx.push(resultType.newInstance());
		    
		    try
	        {
	            while (_ctx.hasMoreTags())
	            {                   
	                for (Marshaller _m : mappers)
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
	        catch (XmlMarshallingException anExc)
	        {
	            throw anExc;
	        }
	        catch (Exception anExc)
	        {
	            throw new XmlMarshallingException(anExc);
	        }
		} catch (XmlMarshallingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new XmlMarshallingException(anExc);
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
