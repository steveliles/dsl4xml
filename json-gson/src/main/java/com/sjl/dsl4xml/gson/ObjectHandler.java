package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.support.Classes;
import com.sjl.dsl4xml.support.Factory;

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
	protected Factory<T,?> factory;
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

	public ObjectHandler(String aName, Factory<T,?> aFactory) {
		name = aName;
		factory = aFactory;
	}

	public ObjectHandler(Factory<T,?> aFactory) {
		name = "";
		factory = aFactory;
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
			T _result = aContext.pop();

			Object _ctx = aContext.peek();
			getMutator(_ctx.getClass(), type, name).apply(_ctx, _result);
		} else if (factory != null) {
			T _result = aContext.pop();
			Object _converted = factory.newTarget(_result);

			Object _ctx = aContext.peek();
			getMutator(_ctx.getClass(), _converted.getClass(), name).apply(_ctx, _converted);
		}
	}

	protected void maybePushNewContextObject(Context aContext) {
		if ((type != null) || (factory != null)) {
			Object _ctx = aContext.peek();
			T _nestedCtx = newContextObject();

			aContext.push(_nestedCtx);
		}
	}

	private T newContextObject() {
		if (type != null) {
			try {
				if (type.isInterface()) {
					return (T) Classes.newDynamicProxy(type);
				} else {
					return (T) type.newInstance();
				}
			} catch (Exception anExc) {
				throw new ParsingException(anExc);
			}
		} else {
			return factory.newIntermediary();
		}
	}

	private ContextMutator getMutator(Class<?> aFor, Class<?> aWith, String aTagName) {
		if (mutator == null) {
			mutator = new ContextMutator(aFor, aWith, aTagName);
		}
		return mutator;
	}

	private class ContextMutator {
		private Method method;
		private String tag;
		private boolean twoArgMutator;

		public ContextMutator(Class<?> aFor, Class<?> aWith, String aTagName) {
			method = Classes.getMutatorMethod(aFor, aWith.getSimpleName(), aTagName);
			twoArgMutator = (method.getParameterTypes().length == 2);
			tag = aTagName;
		}

		public void apply(Object aTo, Object aWith) {
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
