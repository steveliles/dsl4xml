package com.sjl.dsl4xml;

import java.io.*;
import java.util.*;

import org.xmlpull.v1.*;

import com.sjl.dsl4xml.support.*;

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
	private Converter<?>[] converters;
	
	public DocumentMarshaller(Class<T> aClass) {
		resultType = aClass;
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}
	
	public void registerConverters(Converter<?>... aConverters) {
		List<Converter<?>> _converters = new ArrayList<Converter<?>>();
		_converters.addAll(Arrays.asList(aConverters));
		if (converters != null)
			_converters.addAll(Arrays.asList(converters));
		converters = _converters.toArray(new Converter<?>[_converters.size()]);
	}

	public DocumentMarshaller<T> to(Marshaller... aMappers) {
		mappers = aMappers;
		return this;
	}
	
	public T map(InputStream anInputStream, String aCharSet) {
		return marshall(newReader(anInputStream, aCharSet));
	}
	
	public T marshall(Reader aReader)
	throws XmlMarshallingException {
		try {
			XmlPullParser _p = factory.newPullParser();
			_p.setInput(aReader);
			
		    MarshallingContext _ctx = new MarshallingContext(_p);
		    if (converters != null)
		    	_ctx.registerConverters(converters);
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
