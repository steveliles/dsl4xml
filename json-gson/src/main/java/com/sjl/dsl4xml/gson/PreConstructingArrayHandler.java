package com.sjl.dsl4xml.gson;

/**
 * @author steve
 */
public class PreConstructingArrayHandler<T> extends AbstractArrayHandler<T> implements ArrayHandler<T>
{
	public PreConstructingArrayHandler(String aName) {
		super(aName);
	}

	public PreConstructingArrayHandler(String aName, Class<T> aContextType) {
		super(aName, aContextType);
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
}
