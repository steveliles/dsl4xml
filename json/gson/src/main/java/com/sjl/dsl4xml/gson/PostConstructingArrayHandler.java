package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.support.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author steve
 */
public class PostConstructingArrayHandler<I,T> extends AbstractArrayHandler<List> implements ArrayHandler<T>
{
	protected Factory<List<I>,T> factory;

	public PostConstructingArrayHandler(String aName, Factory<List<I>, T> aFactory) {
		super(aName, List.class);
		factory = aFactory;
	}

	public PostConstructingArrayHandler(Factory<List<I>, T> aFactory) {
		super("", List.class);
		factory = aFactory;
	}

	public <R> ArrayHandler<T> of(ObjectHandler<R> aHandler) {
		setHandler(aHandler);
		return this;
	}

	public <R> ArrayHandler<T> of(AbstractArrayHandler<R> aHandler) {
		setHandler(aHandler);
		return this;
	}

	public ArrayHandler<T> of(UnNamedPropertyHandler<?> aHandler) {
		setHandler(aHandler.get());
		return this;
	}

	@Override
	protected List newContextObject() {
		return new ArrayList<I>();
	}

	@Override
	protected void maybePopContext(Context aContext) {
		List<I> _result = aContext.pop();
		Object _converted = factory.newTarget(_result);

		Object _ctx = aContext.peek();
		getMutator(_ctx.getClass(), _converted.getClass(), name).apply(_ctx, _converted);
	}
}
