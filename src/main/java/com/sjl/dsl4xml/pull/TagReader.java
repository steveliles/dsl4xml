package com.sjl.dsl4xml.pull;

import java.lang.reflect.*;
import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

public class TagReader<T> implements XmlReader {

	private String namespace;
	private String tagName;
	private Class<T> type;
	private ContextMutator mutator;
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
	
	public TagReader<T> with(XmlReader... aMappers) {
		if (mappers == null) {
			mappers = new ArrayList<XmlReader>();
		} 
		mappers.addAll(0, Arrays.asList(aMappers));
		
		return this;
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
			
			getMutator(_ctx.getClass(), type).apply(_ctx, _nestedCtx);
			
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
	
	private ContextMutator getMutator(Class<?> aFor, Class<T> aWith) {
		if (mutator == null) {
			mutator = new ContextMutator(aFor, aWith);
		}
		return mutator;
	}
	
	private class ContextMutator {
		private Method method;
		
		public ContextMutator(Class<?> aFor, Class<T> aWith) {
			method = Classes.getMutatorMethod(aFor, aWith.getSimpleName());
		}
		
		public void apply(Object aTo, T aWith) {
			try {
				method.invoke(aTo, aWith);
			} catch (Exception anExc) {
				throw new XmlReadingException(anExc);
			}
		}
	}
}
