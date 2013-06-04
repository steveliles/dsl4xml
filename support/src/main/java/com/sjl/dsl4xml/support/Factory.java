package com.sjl.dsl4xml.support;

/**
 * @deprecated
 *
 * Provides a mechanism for parsing to classes which do not expose mutating
 * methods to allow gradual population, but can only be created in "complete"
 * form via constructors or factory methods.
 *
 * @author steve

 * @param <I> the Intermediate representation used during the parse phase
 * @param <T> the Target representation to return from the result object
 */
public interface Factory<I,R>
{
	/**
	 * Invoked at the end of the parsing phase to create the final target representation.
	 *
	 * @param anIntermediary the fully populated intermediary
	 * @return an instance of the Target type, presumably populated by reading data from
	 *         the intermediary.
	 */
	public R newTarget(I anIntermediary);
}
