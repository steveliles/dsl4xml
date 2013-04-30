package com.sjl.dsl4xml.pull;

import java.lang.reflect.*;
import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

public class TagReader<T> implements ContentReader {

	private String namespace;
	private String tagName;
	private Class<T> type;
	private ContextMutator mutator;
	private AttributesReader attributes;
	private List<XmlReader> mappers;
	
	public TagReader(String aTagName) {
		tagName = aTagName;
	}
	
	public TagReader(String aNamespace, String aTagName) {
		namespace = aNamespace;
		tagName = aTagName;
	}
	
	public TagReader(String aTagName, Class<T> aType) {
		tagName = aTagName;
		type = aType;
	}
	
	public TagReader(String aNamespace, String aTagName, Class<T> aType) {
		namespace = aNamespace;
		tagName = aTagName;
		type = aType;
	}
	
	public TagReader<T> withPCData() {
		return withPCDataMappedTo(tagName);
	}
	
	public TagReader<T> withPCDataMappedTo(final String aFieldName) {
		if (mappers == null) {
			mappers = new ArrayList<XmlReader>();
		}
		
		mappers.add(new PCDataReader<T>(aFieldName));
		
		return this;
	}
	
	public TagReader<T> mappingPCDataTo(XmlReader aMapper) {
		if (mappers == null) {
			mappers = new ArrayList<XmlReader>();
		} 
		mappers.add(aMapper);
		
		return this;
	}
	
	public TagReader<T> with(AttributesReader anAttributes) {
		attributes = anAttributes;
		return this;
	}
	
	public TagReader<T> with(ContentReader... aMappers) {
		if (mappers == null) {
			mappers = new ArrayList<XmlReader>();
		} 
		mappers.addAll(0, Arrays.asList(aMappers));
		
		return this;
	}
	
	public TagReader<T> with(
		AttributesReader anAttributes, ContentReader... aMappers) {
		attributes = anAttributes;
		return (with(aMappers));
	}
	
	@Override
	public boolean read(ReadingContext aContext) {	
		if (mappers == null) {
			withPCDataMappedTo(tagName);
		}

		if (aContext.isStartTagNamed(namespace, tagName)) {		
			maybePushNewContextObject(aContext);
			
			try
	        {
				if (attributes != null)
					attributes.read(aContext);
				
	            while (aContext.isNotEndTag(namespace, tagName))
	            {	            	
	                for (XmlReader _m : mappers)
	                {          	
	                    if (_m.read(aContext))
	                    {
	                        break;
	                    }
	                }
	                aContext.next();
	            }
	            return true;
	        }
	        catch (XmlReadingException anExc)
	        {
	            throw anExc;
	        }
	        catch (Exception anExc)
	        {
	            throw new XmlReadingException(anExc);
	        }
			finally {
				maybePopContext(aContext);
			}
		} else {
			return false;
		}
	}

	private void maybePopContext(ReadingContext aContext) {
		if (type != null) {
			aContext.pop();
		}
	}

	private void maybePushNewContextObject(ReadingContext aContext) {
		if (type != null) {
			Object _ctx = aContext.peek();
			T _nestedCtx = newContextObject();
			
			getMutator(_ctx.getClass(), type, tagName).apply(_ctx, _nestedCtx);
			
			aContext.push(_nestedCtx);
		}
	}
	
	private T newContextObject() {
		try {
			if (type.isInterface()) {		
				return (T) Classes.newDynamicProxy(type);
			} else {		
				return (T) type.newInstance();	
			}
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	private ContextMutator getMutator(Class<?> aFor, Class<T> aWith, String aTagName) {
		if (mutator == null) {
			mutator = new ContextMutator(aFor, aWith, aTagName);
		}
		return mutator;
	}
	
	private class ContextMutator {
		private Method method;
		private String tag;
		private boolean twoArgMutator;
		
		public ContextMutator(Class<?> aFor, Class<T> aWith, String aTagName) {
			method = Classes.getMutatorMethod(aFor, aWith.getSimpleName(), aTagName);
			twoArgMutator = (method.getParameterTypes().length == 2);
			tag = aTagName;
		}
		
		public void apply(Object aTo, T aWith) {
			try {
				if (twoArgMutator) {
					method.invoke(aTo, tag, aWith);
				} else {
					method.invoke(aTo, aWith);
				}
			} catch (Exception anExc) {
				throw new XmlReadingException(anExc);
			}
		}
	}
}
