package com.sjl.dsl4xml;

import java.util.*;

public class TagMapper implements Mapper {

	private String tagName;
	private List<Mapper> mappers;
	
	public TagMapper(String aTagName) {
		tagName = aTagName;
	}

	public TagMapper mappingCDataTo(Mapper aMapper) {
		if (mappers == null) {
			mappers = new ArrayList<Mapper>();
		} 
		mappers.add(aMapper);
		
		return this;
	}
	
	public TagMapper with(Mapper... aMappers) {
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
		} else {
			return false;
		}
	}
}
