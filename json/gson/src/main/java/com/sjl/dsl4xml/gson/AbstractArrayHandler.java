package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.ParsingException;

/**
 * @author steve
 */
abstract class AbstractArrayHandler<T> extends ObjectHandler<T>
{
	private JsonHandler handler;

	protected AbstractArrayHandler(String aName) {
		super(aName);
	}

	public AbstractArrayHandler(String aName, Class<T> aContextType) {
		super(aName, aContextType);
	}

	protected void setHandler(JsonHandler aHandler) {
		handler = aHandler;
	}

	@Override
	public boolean read(Context aContext)
	{
		if (aContext.isStartArrayNamed(name)) {
			maybePushNewContextObject(aContext);
			aContext.next();
			aContext.next(); // advance past BEGIN_ARRAY
			try {
				while (aContext.isNotEndArray()) {
					aContext.prepareForPossibleArrayEntry();

					if (!handler.read(aContext)) {
						aContext.removeUnusedArrayEntry();
					}

					if (aContext.isNotEndArray()) {
						aContext.next();
					} else {
						aContext.next();
						break;
					}
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
