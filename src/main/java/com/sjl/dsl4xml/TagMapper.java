package com.sjl.dsl4xml;

import java.lang.reflect.*;
import java.util.*;

import com.sjl.dsl4xml.support.*;

public class TagMapper<T> implements Mapper {

	private String tagName;
	private Class<T> type;
	private ContextMutator mutator;
	private List<Mapper> mappers;
	
	public TagMapper(String aTagName) {
		tagName = aTagName;
	}
	
	public TagMapper(String aTagName, Class<T> aType) {
		tagName = aTagName;
		type = aType;
	}
	
	public TagMapper<T> withCDataMappedTo(final String aFieldName) {
		if (mappers == null) {
			mappers = new ArrayList<Mapper>();
		}
		
		mappers.add(new CDataMapper() {
			private Method mutator;

			@SuppressWarnings("unchecked")
			public void map(MappingContext aContext, String aText) {
				T _ctx = aContext.peek();
				
				try {
					getMutator((Class<T>)_ctx.getClass(), aFieldName).invoke(_ctx, aText); // TODO: type conversion
				} catch (Exception anExc) {
					throw new XmlParseException(anExc);
				}
			}
			
			private Method getMutator(Class<T> aClass, String aFieldName) {
				if (mutator == null) {
					mutator = Classes.getMutatorMethod(aClass, aFieldName);
				}
				return mutator;
			}
		});
		return this;
	}
	
	public TagMapper<T> mappingCDataTo(Mapper aMapper) {
		if (mappers == null) {
			mappers = new ArrayList<Mapper>();
		} 
		mappers.add(aMapper);
		
		return this;
	}
	
	public TagMapper<T> with(Mapper... aMappers) {
		if (mappers == null) {
			mappers = new ArrayList<Mapper>();
		} 
		mappers.addAll(0, Arrays.asList(aMappers));
		
		return this;
	}
	
	@Override
	public boolean map(MappingContext aContext) {
		if (
			(aContext.isStartTag()) &&
			(aContext.isTagNamed(tagName))
		) {	
			if (type != null) {
				Object _ctx = aContext.peek();
				T _nestedCtx = newContextObject();
				
				getMutator(_ctx.getClass(), type).apply(_ctx, _nestedCtx);
				
				aContext.push(_nestedCtx);
			}
			
			try
	        {
	            while (aContext.isNotEndTag(tagName))
	            {	            	
	                for (Mapper _m : mappers)
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
	        catch (XmlParseException anExc)
	        {
	            throw anExc;
	        }
	        catch (Exception anExc)
	        {
	            throw new XmlParseException(anExc);
	        }
			finally {
				if (type != null) {
					aContext.pop();
				}
			}
		} else {
			return false;
		}
	}
	
	private T newContextObject() {
		try {
			return type.newInstance();
		} catch (Exception anExc) {
			throw new XmlParseException(anExc);
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
				throw new XmlParseException(anExc);
			}
		}
	}
}
