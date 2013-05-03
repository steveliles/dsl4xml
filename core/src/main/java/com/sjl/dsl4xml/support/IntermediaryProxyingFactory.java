package com.sjl.dsl4xml.support;

/**
 * @author steve
 */
public abstract class IntermediaryProxyingFactory<I,R> implements Factory<I,R> {

	private Class<I> intermediaryClass;

	public IntermediaryProxyingFactory(Class<I> anIntermediaryClass) {
		intermediaryClass = anIntermediaryClass;
	}

	@Override
	public I newIntermediary() {
		return Classes.newDynamicProxy(intermediaryClass);
	}
}
