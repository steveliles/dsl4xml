package com.sjl.dsl4xml;

import java.io.*;
import java.util.*;

import org.xmlpull.v1.*;

import com.sjl.dsl4xml.support.*;

public class DocumentReader<T> {

	public static <R> DocumentReader<R> mappingOf(Class<R> aClass) {
		return new DocumentReader<R>(aClass);
	}
	
	public static <R> TagReader<R> tag(String aTagName) {
		return new TagReader<R>(aTagName);
	}
	
	public static <R> TagReader<R> tag(String aNamespace, String aTagName) {
		return new TagReader<R>(aNamespace, aTagName);
	}
	
	public static <R> TagReader<R> tag(String aTagName, Class<R> aContextType) {
		return new TagReader<R>(aTagName, aContextType);
	}
	
	public static <R> TagReader<R> tag(String aNamespace, String aTagName, Class<R> aContextType) {
		return new TagReader<R>(aNamespace, aTagName, aContextType);
	}
	
	/**
	 * @param array of attribute names
	 * @return a NamedAttributesReader that looks up attribute values by name.
	 * Useful if some attributes are being ignored.
	 */
	public static NamedAttributesReader attributes(String... anAttributeNames) {
		return new NamedAttributesReader(anAttributeNames);
	}
	
	// TODO: multi-attributes with namespace?
	
	public static NamedAttributesReader attribute(String anAttributeName) {
		return new NamedAttributesReader(anAttributeName);
	}
	
	public static NamedAttributesReader attribute(String aNamespace, String anAttributeName) {
		return new NamedAttributesReader(aNamespace, anAttributeName);
	}
	
	public static <T> PCDataReader<T> pcdataMappedTo(String aFieldName) {
		return new PCDataReader<T>(aFieldName);
	}
	
	/**
	 * @param array of setter-method names (without the prefix)
	 * @return an OrderedAttributesReader that should be marginally quicker 
	 * than a NamedAttributesReader due to attribute value lookup by index
	 * rather than by name. 
	 */
	public static OrderedAttributesReader attributesInOrder(String... aSetterMethodNames) {
		return new OrderedAttributesReader(aSetterMethodNames);
	}
	
	private XmlPullParserFactory factory;
	private XmlReader[] mappers;
	private Class<T> resultType;
	private Converter<?>[] converters;
	
	public DocumentReader(Class<T> aClass) {
		resultType = aClass;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
		} catch (XmlPullParserException anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	public void registerConverters(Converter<?>... aConverters) {
		List<Converter<?>> _converters = new ArrayList<Converter<?>>();
		_converters.addAll(Arrays.asList(aConverters));
		if (converters != null)
			_converters.addAll(Arrays.asList(converters));
		converters = _converters.toArray(new Converter<?>[_converters.size()]);
	}

	public DocumentReader<T> to(XmlReader... aMappers) {
		mappers = aMappers;
		return this;
	}
	
	public T read(InputStream anInputStream, String aCharSet) {
		return read(newReader(anInputStream, aCharSet));
	}
	
	public T read(Reader aReader)
	throws XmlReadingException {
		try {
			XmlPullParser _p = factory.newPullParser();
			_p.setInput(aReader);
			
		    ReadingContext _ctx = new PullParserReadingContext(_p);
		    if (converters != null)
		    	_ctx.registerConverters(converters);
		    _ctx.push(resultType.newInstance());
		    
		    try
	        {
	            while (_ctx.hasMoreTags())
	            {	       
	                for (XmlReader _m : mappers)
	                {                 
	                    if (_m.read(_ctx))
	                    {                            
	                        break;
	                    }
	                }
	                
	                if (_ctx.hasMoreTags())
	                	_ctx.next();
	            }
	            return _ctx.peek();
	        }
	        catch (XmlReadingException anExc)
	        {
	            throw anExc;
	        }
	        catch (Exception anExc)
	        {
	            throw new XmlReadingException(anExc);
	        }
		} catch (XmlReadingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
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
