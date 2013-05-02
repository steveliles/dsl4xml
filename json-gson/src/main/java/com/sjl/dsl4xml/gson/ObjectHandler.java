package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.support.Classes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author steve
 */
public class ObjectHandler<T> implements JsonHandler
{
	protected String name;
	protected Class<T> type;
	protected List<JsonHandler> handlers;
	private ContextMutator mutator;

	public ObjectHandler(String aName) {
		name = aName;
	}

	public ObjectHandler(String aName, Class<T> aContextType) {
		name = aName;
		type = aContextType;
	}

	public ObjectHandler(Class<T> aContextType) {
		name = "";
		type = aContextType;
	}

	@Override
	public boolean read(Context aContext)
	{
		if (aContext.isStartObjectNamed(name)) {
			maybePushNewContextObject(aContext);
			aContext.next(); // advance past BEGIN_OBJECT
			try {
				while (aContext.isNotEndObject()) {
					for (JsonHandler _h : handlers) {
						if (_h.read(aContext)) {
							break;
						}
					}

					if (aContext.peek() == JsonToken.END_OBJECT) {
						aContext.next();
						break;
					} else {
						aContext.next();
					}

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

	public ObjectHandler<T> with(JsonHandler... aHandlers) {
		if (handlers == null) {
			handlers = new ArrayList<JsonHandler>();
		}
		handlers.addAll(0, Arrays.asList(aHandlers));

		return this;
	}

	protected void maybePopContext(Context aContext) {
		if (type != null) {
			aContext.pop();
		}
	}

	protected void maybePushNewContextObject(Context aContext) {
		if (type != null) {
			Object _ctx = aContext.peek();
			T _nestedCtx = newContextObject();

			getMutator(_ctx.getClass(), type, name).apply(_ctx, _nestedCtx);

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
			throw new ParsingException(anExc);
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
				throw new ParsingException(anExc);
			}
		}
	}

	@Override
	public String toString() {
		return "object-handler:" + System.identityHashCode(this);
	}
}