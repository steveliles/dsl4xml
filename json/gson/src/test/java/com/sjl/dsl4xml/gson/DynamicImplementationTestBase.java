package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.DocumentReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class DynamicImplementationTestBase
{

	public interface Root {
		public String getElem1();
		public String getElem2();
	}

	public interface Book {
		public String getTitle();
		public void setTitle(String aTitle);

		public String getAuthor();
		public void setAuthor(String anAuthor);
	}

	public interface Profile1 {
		public String getName();
		public void setName(String aName);

		public List<Book> getReadingList();
		public void setReadingList(List<Book> aBooks);
	}

	public interface Profile2 extends List<Book> {
		public String getName();
		public void setName(String aName);
	}

	public interface Person {
		public String getName();
		public Date getDateOfBirth();
		public int getNumberOfDependents();
	}

	protected abstract DocumentReader<Root> newRootOnlyUnmarshaller();

	protected abstract DocumentReader<Root> newCorrectRootUnmarshaller();

	protected abstract DocumentReader<Root> newMissingElementRootUnmarshaller();

	protected abstract DocumentReader<Profile1> newProfile1Unmarshaller();

	protected abstract DocumentReader<Profile2> newProfile2Unmarshaller();

	protected abstract DocumentReader<Person> newPersonUnmarshaller();

	@Test
	public void unmarshallsRootElementToDynamicallyImplementedRootType() {
		DocumentReader<Root> _r = newRootOnlyUnmarshaller();
		Root _root = _r.read(get("root-1.json"), "utf-8");
		Assert.assertNotNull(_root);
	}

	@Test
	public void unmarshallsRootElementWithMissingElements() {
		DocumentReader<Root> _r = newMissingElementRootUnmarshaller();
		Root _root = _r.read(get("root-2.json"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertNull(_root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}

	@Test
	public void unmarshallsRootElementWithAllElements() {
		DocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-3.json"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertEquals("one", _root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}

	@Test
	public void dynamicImplementationsProduceReasonableToString() {
		DocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-3.json"), "utf-8");

		Assert.assertTrue(_root.toString().startsWith("proxy(" + Root.class.getName() + "){"));
		Assert.assertTrue(_root.toString().contains("elem1=one"));
		Assert.assertTrue(_root.toString().contains("elem2=two"));
	}

	@Test
	public void dynamicImplementationsCanIncorporateListsOfRepeatedElements() {
		DocumentReader<Profile1> _r = newProfile1Unmarshaller();
		Profile1 _profile = _r.read(get("profile-1.json"), "utf-8");
		Assert.assertNotNull(_profile);

		Assert.assertEquals("Steve", _profile.getName());
		Assert.assertEquals(2, _profile.getReadingList().size());

		Book _first = _profile.getReadingList().get(0);
		Book _second = _profile.getReadingList().get(1);

		Assert.assertEquals("Java for Dummies", _first.getTitle());
		Assert.assertEquals("Somebody Else", _second.getAuthor());
	}

	@Test
	public void dynamicImplementationsCanIncorporateInlineListsOfRepeatedElements() {
		DocumentReader<Profile2> _r = newProfile2Unmarshaller();
		Profile2 _profile = _r.read(get("profile-2.json"), "utf-8");
		Assert.assertNotNull(_profile);

		Assert.assertEquals("Steve", _profile.getName());
		Assert.assertEquals(2, _profile.size());

		Book _first = _profile.get(0);
		Book _second = _profile.get(1);

		Assert.assertEquals("Java for Dummies", _first.getTitle());
		Assert.assertEquals("Somebody Else", _second.getAuthor());
	}

	@Test
	public void dynamicImplementationsCanConvertTypes()
	throws Exception {
		DocumentReader<Person> _r = newPersonUnmarshaller();
		Person _person = _r.read(get("person-1.json"), "utf-8");
		Assert.assertNotNull(_person);

		Assert.assertEquals("joe bloggs", _person.getName());
		Assert.assertEquals(
		new SimpleDateFormat("yyyyMMdd").parse("19770526"),
		_person.getDateOfBirth());
		Assert.assertEquals(3, _person.getNumberOfDependents());
	}

	protected InputStream get(String anXml) {
		return DynamicImplementationTestBase.class.getResourceAsStream(anXml);
	}

}
