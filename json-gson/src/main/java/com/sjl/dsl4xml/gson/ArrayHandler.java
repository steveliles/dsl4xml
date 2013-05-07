package com.sjl.dsl4xml.gson;

/**
 * @author steve
 */
public interface ArrayHandler<T> extends JsonHandler
{
	public <R> ArrayHandler<T> of(ObjectHandler<R> aHandler);

	public <R> ArrayHandler<T> of(AbstractArrayHandler<R> aHandler);

	public ArrayHandler<T> of(UnNamedPropertyHandler<?> aHandler);
}
