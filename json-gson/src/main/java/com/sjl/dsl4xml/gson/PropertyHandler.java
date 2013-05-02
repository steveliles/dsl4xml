package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.support.ValueSetter;

/**
 * @author steve
 */
public class PropertyHandler<T> implements JsonHandler
{
	private String name;
	private ValueSetter setter;

	public PropertyHandler(String aName) {
		name = aName;
	}

	@Override
	public boolean read(Context aContext)
	{
		if (aContext.isPropertyNamed(name)) {
			read(aContext, aContext.getValue());
			return true;
		}
		return false;
	}

	public void read(Context aContext, String aText) {
		T _currentContext = aContext.peek();

		try {
			ValueSetter _vs = getSetter(aContext, _currentContext.getClass(), name);
			_vs.invoke(name, _currentContext, aText);
		} catch (ParsingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new ParsingException(anExc);
		}
	}

	private ValueSetter getSetter(Context aContext, Class<?> aClass, String aFieldName) {
		if (setter == null) {
			setter = new ValueSetter(aContext, aClass, aFieldName);
		}
		return setter;
	}

	@Override
	public String toString() {
		return "property-handler:" + System.identityHashCode(this);
	}
}
