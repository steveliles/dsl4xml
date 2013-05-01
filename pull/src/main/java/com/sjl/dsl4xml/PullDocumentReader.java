package com.sjl.dsl4xml;

import java.io.*;

import org.xmlpull.v1.*;

import com.sjl.dsl4xml.pull.*;

public class PullDocumentReader<T> extends AbstractDocumentReader<T> {

	public static <R> PullDocumentReader<R> mappingOf(Class<R> aClass) {
		return new PullDocumentReader<R>(aClass);
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
	public static AttributesReader attributes(String... anAttributeNames) {
		return new NamedAttributesReader(anAttributeNames);
	}
	
	// TODO: multi-attributes with namespace?
	
	public static AttributesReader attribute(String anAttributeName) {
		return new NamedAttributesReader(anAttributeName);
	}
	
	public static AttributesReader attribute(String aNamespace, String anAttributeName) {
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
	public static AttributesReader attributesInOrder(String... aSetterMethodNames) {
		return new OrderedAttributesReader(aSetterMethodNames);
	}
	
	private XmlPullParserFactory factory;
	private XmlReader[] mappers = new XmlReader[]{};
	
	public PullDocumentReader(Class<T> aClass) {
		super(aClass);
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
		} catch (XmlPullParserException anExc) {
			throw new ParsingException(anExc);
		}
	}
	
	public PullDocumentReader<T> to(XmlReader... aMappers) {
		mappers = aMappers;
		return this;
	}
	
	@Override
	public T read(Reader aReader)
	throws ParsingException
	{
		try {
			XmlPullParser _p = factory.newPullParser();
			_p.setInput(aReader);
			
		    ReadingContext _ctx = new PullParserReadingContext(_p);
		    if (converters != null)
		    	_ctx.registerConverters(converters);
		    _ctx.push(newResultObject());
		    
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
	        catch (ParsingException anExc)
	        {
	            throw anExc;
	        }
	        catch (Exception anExc)
	        {
	            throw new ParsingException(anExc);
	        }
		} catch (ParsingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new ParsingException(anExc);
		}
	}
}
