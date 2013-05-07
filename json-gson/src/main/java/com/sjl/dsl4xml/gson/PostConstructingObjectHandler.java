package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.support.Factory;

/**
 * @author steve
 */
public class PostConstructingObjectHandler<I,T>
extends ObjectHandler<I>
{
	protected Factory<I,T> factory;

	public PostConstructingObjectHandler(String aName, Class<I> aContextType, Factory<I, T> aFactory) {
		super(aName, aContextType);
		factory = aFactory;
	}

	public PostConstructingObjectHandler(Class<I> aContextType, Factory<I, T> aFactory) {
		super("", aContextType);
		factory = aFactory;
	}

	@Override
	protected void maybePopContext(Context aContext) {
		I _result = aContext.pop();
		Object _converted = factory.newTarget(_result);

		Object _ctx = aContext.peek();
		getMutator(_ctx.getClass(), _converted.getClass(), name).apply(_ctx, _converted);
	}
}
