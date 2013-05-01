package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.DocumentReader;
import com.sjl.dsl4xml.support.convert.ThreadUnsafeDateConverter;

import java.util.List;

import static com.sjl.dsl4xml.GsonDocumentReader.*;

public class DynamicImplementationGsonParserTest extends DynamicImplementationTestBase
{

	@Override
	protected DocumentReader<Root> newRootOnlyUnmarshaller() {
		return mappingOf(Root.class);
	}

	protected DocumentReader<Root> newCorrectRootUnmarshaller() {
		return mappingOf(Root.class).to(
			object("elem1"),
			object("elem2")
		);
	}

	@Override
	protected DocumentReader<Root> newMissingElementRootUnmarshaller() {
		return mappingOf(Root.class).to(
			object("missing", String.class)
		);
	}

	protected DocumentReader<Profile1> newProfile1Unmarshaller() {
		return mappingOf(Profile1.class).to(
			object("name"),
			object("readingList", List.class).with(
				object("book", Book.class).with(
					object("title"), object("author")
				)
			)
		);
	}
	
	protected DocumentReader<Profile2> newProfile2Unmarshaller() {
		return mappingOf(Profile2.class).to(
			object("name"),
			object("book", Book.class).with(
				object("title"), object("author")
			)
		);
	}
	
	protected DocumentReader<Person> newPersonUnmarshaller() {
		DocumentReader<Person> _r = mappingOf(Person.class).to(
			object("name"),
			object("dateOfBirth"),
			object("numberOfDependents")
		);
		
		_r.registerConverters(new ThreadUnsafeDateConverter("yyyyMMdd"));
		
		return _r;
	}
	
}
