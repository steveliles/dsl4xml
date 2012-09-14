package com.sjl.dsl4xml.sax;

import static com.sjl.dsl4xml.SAXDocumentReader.*;

import java.util.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.convert.*;

public class DynamicImplementationSAXParserTest extends DynamicImplementationTestBase {

	@Override
	protected DocumentReader<Root> newRootOnlyUnmarshaller() {
		return mappingOf("root", Root.class);
	}

	protected DocumentReader<Root> newCorrectRootUnmarshaller() {
		return mappingOf("root", Root.class).to(
			attributes("attr1", "attr2"),
			tag("elem1"),
			tag("elem2")
		);
	}
	
	@Override
	protected DocumentReader<Root> newMissingAttributeRootUnmarshaller() {
		return mappingOf("root", Root.class).to(
			attributes("attr1", "attr2", "attr3")
		);
	}
	
	@Override
	protected DocumentReader<Root> newMissingElementRootUnmarshaller() {
		return mappingOf("root", Root.class).to(
			tag("missing", String.class)
		);
	}

	protected DocumentReader<Profile1> newProfile1Unmarshaller() {
		return mappingOf("profile", Profile1.class).to(
			tag("name"),
			tag("readingList", List.class).with(
				tag("book", Book.class).with(
					tag("title"), tag("author")
				)
			)
		);
	}
	
	protected DocumentReader<Profile2> newProfile2Unmarshaller() {
		return mappingOf("profile", Profile2.class).to(
			tag("name"),
			tag("book", Book.class).with(
				tag("title"), tag("author")
			)
		);
	}
	
	protected DocumentReader<Person> newPersonUnmarshaller() {
		DocumentReader<Person> _r = mappingOf("person", Person.class).to(
			tag("name"),
			tag("dateOfBirth"),
			tag("numberOfDependents")
		);
		
		_r.registerConverters(new ThreadUnsafeDateConverter("yyyyMMdd"));
		
		return _r;
	}
	
}
