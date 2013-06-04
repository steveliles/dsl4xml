package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.LegacyDocumentReader;
import com.sjl.dsl4xml.support.convert.ThreadUnsafeDateStringConverter;

import java.util.List;

import static com.sjl.dsl4xml.GsonLegacyDocumentReader.*;

public class DynamicImplementationGsonParserTest extends DynamicImplementationTestBase
{

	@Override
	protected LegacyDocumentReader<Root> newRootOnlyUnmarshaller() {
		return mappingOf(Root.class);
	}

	protected LegacyDocumentReader<Root> newCorrectRootUnmarshaller() {
		return mappingOf(Root.class).to(
			property("elem1"),
			property("elem2")
		);
	}

	@Override
	protected LegacyDocumentReader<Root> newMissingElementRootUnmarshaller() {
		return mappingOf(Root.class).to(
			property("elem2")
		);
	}

	protected LegacyDocumentReader<Profile1> newProfile1Unmarshaller() {
		return mappingOf(Profile1.class).to(
			property("name"),
			array("readingList", List.class).of(
				object(Book.class).with(
					property("title"), property("author")
				)
			)
		);
	}
	
	protected LegacyDocumentReader<Profile2> newProfile2Unmarshaller() {
		return mappingOf(Profile2.class).to(
			property("name"),
			object("book", Book.class).with(
				property("title"), property("author")
			)
		);
	}
	
	protected LegacyDocumentReader<Person> newPersonUnmarshaller() {
		LegacyDocumentReader<Person> _r = mappingOf(Person.class).to(
			property("name"),
			property("dateOfBirth"),
			property("numberOfDependents")
		);
		
		_r.registerConverters(new ThreadUnsafeDateStringConverter("yyyyMMdd"));
		
		return _r;
	}
	
}
