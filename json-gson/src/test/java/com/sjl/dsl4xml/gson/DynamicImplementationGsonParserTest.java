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
			property("elem1"),
			property("elem2")
		);
	}

	@Override
	protected DocumentReader<Root> newMissingElementRootUnmarshaller() {
		return mappingOf(Root.class).to(
			property("elem2")
		);
	}

	protected DocumentReader<Profile1> newProfile1Unmarshaller() {
		return mappingOf(Profile1.class).to(
			property("name"),
			array("readingList", List.class).of(
				object(Book.class).with(
					property("title"), property("author")
				)
			)
		);
	}
	
	protected DocumentReader<Profile2> newProfile2Unmarshaller() {
		return mappingOf(Profile2.class).to(
			property("name"),
			object("book", Book.class).with(
				property("title"), property("author")
			)
		);
	}
	
	protected DocumentReader<Person> newPersonUnmarshaller() {
		DocumentReader<Person> _r = mappingOf(Person.class).to(
			property("name"),
			property("dateOfBirth"),
			property("numberOfDependents")
		);
		
		_r.registerConverters(new ThreadUnsafeDateConverter("yyyyMMdd"));
		
		return _r;
	}
	
}
