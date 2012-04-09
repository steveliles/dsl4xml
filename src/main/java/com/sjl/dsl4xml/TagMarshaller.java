package com.sjl.dsl4xml;

import java.lang.reflect.*;
import java.util.*;

import com.sjl.dsl4xml.support.*;

public class TagMarshaller<T> implements Marshaller {

	private String tagName;
	private Class<T> type;
	private ContextMutator mutator;
	private List<Marshaller> mappers;
	
	public TagMarshaller(String aTagName) {
		tagName = aTagName;
	}
	
	public TagMarshaller(String aTagName, Class<T> aType) {
		tagName = aTagName;
		type = aType;
	}
	
	public TagMarshaller<T> withPCData() {
		return withPCDataMappedTo(tagName);
	}
	
	public TagMarshaller<T> withPCDataMappedTo(final String aFieldName) {
		if (mappers == null) {
			mappers = new ArrayList<Marshaller>();
		}
		
		mappers.add(new PCDataMarshaller<T>(aFieldName));
		
		return this;
	}
	
	public TagMarshaller<T> mappingPCDataTo(Marshaller aMapper) {
		if (mappers == null) {
			mappers = new ArrayList<Marshaller>();
		} 
		mappers.add(aMapper);
		
		return this;
	}
	
	public TagMarshaller<T> with(Marshaller... aMappers) {
		if (mappers == null) {
			mappers = new ArrayList<Marshaller>();
		} 
		mappers.addAll(0, Arrays.asList(aMappers));
		
		return this;
	}
	
	@Override
	public boolean map(MarshallingContext aContext) {
		if (mappers == null) {
			withPCDataMappedTo(tagName);
		}
		
		if (aContext.isStartTagNamed(tagName)) {	
			maybePushNewContextObject(aContext);
			
			try
	        {
	            while (aContext.isNotEndTag(tagName))
	            {	            	
	                for (Marshaller _m : mappers)
	                {          	
	                    if (_m.map(aContext))
	                    {                            
	                        break;
	                    }
	                }
	                aContext.next();
	            }
	            return true;
	        }
	        catch (XmlMarshallingException anExc)
	        {
	            throw anExc;
	        }
	        catch (Exception anExc)
	        {
	            throw new XmlMarshallingException(anExc);
	        }
			finally {
				maybePopContext(aContext);
			}
		} else {
			return false;
		}
	}

	private void maybePopContext(MarshallingContext aContext) {
		if (type != null) {
			aContext.pop();
		}
	}

	private void maybePushNewContextObject(MarshallingContext aContext) {
		if (type != null) {
			Object _ctx = aContext.peek();
			T _nestedCtx = newContextObject();
			
			getMutator(_ctx.getClass(), type).apply(_ctx, _nestedCtx);
			
			aContext.push(_nestedCtx);
		}
	}
	
	private T newContextObject() {
		try {
			return type.newInstance();
		} catch (Exception anExc) {
			throw new XmlMarshallingException(anExc);
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
				throw new XmlMarshallingException(anExc);
			}
		}
	}
}
