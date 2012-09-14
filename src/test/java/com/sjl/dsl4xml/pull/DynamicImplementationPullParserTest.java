package com.sjl.dsl4xml.pull;

import static com.sjl.dsl4xml.PullDocumentReader.*;

import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.convert.*;

public class DynamicImplementationPullParserTest extends DynamicImplementationTestBase {

	@Override
	protected DocumentReader<Root> newRootOnlyUnmarshaller() {
		return mappingOf(Root.class);
	}

	protected DocumentReader<Root> newCorrectRootUnmarshaller() {
		return mappingOf(Root.class).to(
			attributes("attr1", "attr2"),
			tag("elem1"),
			tag("elem2")
		);
	}
	
	@Override
	protected DocumentReader<Root> newMissingAttributeRootUnmarshaller() {
		return mappingOf(Root.class).to(
			attributes("attr1", "attr2", "attr3")
		);
	}
	
	@Override
	protected DocumentReader<Root> newMissingElementRootUnmarshaller() {
		return mappingOf(Root.class).to(
			tag("missing", String.class)
		);
	}

	protected DocumentReader<Profile1> newProfile1Unmarshaller() {
		return mappingOf(Profile1.class).to(
			tag("name"),
			tag("readingList", List.class).with(
				tag("book", Book.class).with(
					tag("title"), tag("author")
				)
			)
		);
	}
	
	protected DocumentReader<Profile2> newProfile2Unmarshaller() {
		return mappingOf(Profile2.class).to(
			tag("name"),
			tag("book", Book.class).with(
				tag("title"), tag("author")
			)
		);
	}
	
	protected DocumentReader<Person> newPersonUnmarshaller() {
		DocumentReader<Person> _r = mappingOf(Person.class).to(
			tag("name"),
			tag("dateOfBirth"),
			tag("numberOfDependents")
		);
		
		_r.registerConverters(new ThreadUnsafeDateConverter("yyyyMMdd"));
		
		return _r;
	}
	
}
