package com.sjl.dsl4xml.sax;

import java.io.*;
import java.text.*;
import java.util.*;

import com.sjl.dsl4xml.LegacyDocumentReader;
import org.junit.*;

public abstract class DynamicImplementationTestBase {

	public interface Root {
		public String getAttr1();
		public void setAttr1(String anAttr);

		public String getAttr2();
		public void setAttr2(String anAttr);

		public String getElem1();
		public void setElem1(String aValue);

		public String getElem2();
		public void setElem2(String aValue);
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

	protected abstract LegacyDocumentReader<Root> newRootOnlyUnmarshaller();

	protected abstract LegacyDocumentReader<Root> newCorrectRootUnmarshaller();

	protected abstract LegacyDocumentReader<Root> newMissingAttributeRootUnmarshaller();

	protected abstract LegacyDocumentReader<Root> newMissingElementRootUnmarshaller();

	protected abstract LegacyDocumentReader<Profile1> newProfile1Unmarshaller();

	protected abstract LegacyDocumentReader<Profile2> newProfile2Unmarshaller();

	protected abstract LegacyDocumentReader<Person> newPersonUnmarshaller();

	@Test
	public void unmarshallsRootElementToDynamicallyImplementedRootType() {
		LegacyDocumentReader<Root> _r = newRootOnlyUnmarshaller();
		Root _root = _r.read(get("root-1.xml"), "utf-8");
		Assert.assertNotNull(_root);
	}

	@Test
	public void unmarshallsRootElementWithSomeAttributes() {
		LegacyDocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-2.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertNull(_root.getAttr1());
		Assert.assertEquals("second", _root.getAttr2());
	}

	@Test
	public void unmarshallsRootElementWithNoAttributesAndMissingElements() {
		LegacyDocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-3.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertNull(_root.getAttr1());
		Assert.assertNull(_root.getAttr2());
		Assert.assertNull(_root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}

	@Test
	public void unmarshallsRootElementWithMissingAttributesAndElements() {
		LegacyDocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-4.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertNull(_root.getAttr1());
		Assert.assertEquals("second", _root.getAttr2());
		Assert.assertNull(_root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}

	@Test
	public void unmarshallsRootElementWithAllAttributesAndElements() {
		LegacyDocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-5.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertEquals("first", _root.getAttr1());
		Assert.assertEquals("second", _root.getAttr2());
		Assert.assertEquals("one", _root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}

	@Test
	public void dynamicImplementationsProduceReasonableToString() {
		LegacyDocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-5.xml"), "utf-8");

		Assert.assertTrue(_root.toString().startsWith("proxy(" + Root.class.getName() + "){"));
		Assert.assertTrue(_root.toString().contains("attr1=first"));
		Assert.assertTrue(_root.toString().contains("attr2=second"));
		Assert.assertTrue(_root.toString().contains("elem1=one"));
		Assert.assertTrue(_root.toString().contains("elem2=two"));
	}

	@Test
	public void dynamicImplementationsCanIncorporateListsOfRepeatedElements() {
		LegacyDocumentReader<Profile1> _r = newProfile1Unmarshaller();
		Profile1 _profile = _r.read(get("profile-1.xml"), "utf-8");
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
		LegacyDocumentReader<Profile2> _r = newProfile2Unmarshaller();
		Profile2 _profile = _r.read(get("profile-2.xml"), "utf-8");
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
		LegacyDocumentReader<Person> _r = newPersonUnmarshaller();
		Person _person = _r.read(get("person-1.xml"), "utf-8");
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
