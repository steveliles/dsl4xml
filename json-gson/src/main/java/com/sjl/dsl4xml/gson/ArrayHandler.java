package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.ParsingException;

/**
 * @author steve
 */
public class ArrayHandler<T> extends ObjectHandler<T>
{
	private JsonHandler handler;

	public ArrayHandler(String aName, Class<T> aContextType) {
		super(aName, aContextType);
	}

	public ArrayHandler of(JsonHandler aHandler) {
		handler = aHandler;
		return this;
	}

	@Override
	public boolean read(Context aContext)
	{
		if (aContext.isStartArrayNamed(name)) {
			maybePushNewContextObject(aContext);
			aContext.next(); // advance past BEGIN_ARRAY
			try {
				while (aContext.isNotEndArray()) {
					aContext.prepareForPossibleArrayEntry();

					if (!handler.read(aContext))
						aContext.removeUnusedArrayEntry();

					aContext.next();

					if (!aContext.hasNext())
						break;
				}

				return true;
			} catch (ParsingException anExc) {
				throw anExc;
			} catch (Exception anExc) {
				throw new ParsingException(anExc);
			} finally {
				maybePopContext(aContext);
			}
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "array-handler:" + System.identityHashCode(this);
	}
}
