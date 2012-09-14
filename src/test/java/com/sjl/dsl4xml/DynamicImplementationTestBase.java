package com.sjl.dsl4xml;

import java.io.*;
import java.text.*;
import java.util.*;

import org.junit.*;

import com.sjl.dsl4xml.support.*;

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
		public void setName(String aName);
		
		public Date getDateOfBirth();
		public void setDateOfBirth(Date aDateOfBirth);
		
		public int getNumberOfDependents();
		public void setNumberOfDependents(int aNumber);
	}
	
	protected abstract DocumentReader<Root> newRootOnlyUnmarshaller();
	
	protected abstract DocumentReader<Root> newCorrectRootUnmarshaller();
	
	protected abstract DocumentReader<Root> newMissingAttributeRootUnmarshaller();
	
	protected abstract DocumentReader<Root> newMissingElementRootUnmarshaller();
	
	protected abstract DocumentReader<Profile1> newProfile1Unmarshaller();
	
	protected abstract DocumentReader<Profile2> newProfile2Unmarshaller();
	
	protected abstract DocumentReader<Person> newPersonUnmarshaller();
	
	@Test
	public void unmarshallsRootElementToDynamicallyImplementedRootType() {
		DocumentReader<Root> _r = newRootOnlyUnmarshaller();
		Root _root = _r.read(get("root-1.xml"), "utf-8");
		Assert.assertNotNull(_root);
	}
	
	@Test
	public void unmarshallsRootElementWithSomeAttributes() {
		DocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-2.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertNull(_root.getAttr1());
		Assert.assertEquals("second", _root.getAttr2());
	}
	
	@Test
	public void unmarshallsRootElementWithNoAttributesAndMissingElements() {
		DocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-3.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertNull(_root.getAttr1());
		Assert.assertNull(_root.getAttr2());
		Assert.assertNull(_root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}
	
	@Test
	public void unmarshallsRootElementWithMissingAttributesAndElements() {
		DocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-4.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertNull(_root.getAttr1());
		Assert.assertEquals("second", _root.getAttr2());
		Assert.assertNull(_root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}
	
	@Test
	public void unmarshallsRootElementWithAllAttributesAndElements() {
		DocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-5.xml"), "utf-8");
		Assert.assertNotNull(_root);
		Assert.assertEquals("first", _root.getAttr1());
		Assert.assertEquals("second", _root.getAttr2());
		Assert.assertEquals("one", _root.getElem1());
		Assert.assertEquals("two", _root.getElem2());
	}
	
	@Test
	public void givesGoodErrorForMissingAttributeSetMethod() {
		try {
			DocumentReader<Root> _r = newMissingAttributeRootUnmarshaller();
			
			_r.read(get("root-6.xml"), "utf-8");
			Assert.fail("should have thrown exception");
		} catch (NoSuitableMethodException anExc) {
			Assert.assertTrue(
				anExc.getMessage().contains("setAttr3") &&
				anExc.getMessage().contains("putAttr3") &&
				anExc.getMessage().contains("addAttr3") &&
				anExc.getMessage().contains("set") &&
				anExc.getMessage().contains("add") &&
				anExc.getMessage().contains("put")
			);
		}
	}
	
	@Test
	public void givesGoodErrorForMissingElementSetMethod() {
		try {
			DocumentReader<Root> _r = newMissingElementRootUnmarshaller();
			
			_r.read(get("root-6.xml"), "utf-8");
			Assert.fail("should have thrown exception");
		} catch (NoSuitableMethodException anExc) {	
			Assert.assertTrue(anExc.getMessage().contains("setMissing"));
			Assert.assertTrue(anExc.getMessage().contains("putMissing"));
			Assert.assertTrue(anExc.getMessage().contains("addMissing"));
			Assert.assertTrue(anExc.getMessage().contains("setString"));
			Assert.assertTrue(anExc.getMessage().contains("putString"));
			Assert.assertTrue(anExc.getMessage().contains("addString"));
			Assert.assertTrue(anExc.getMessage().contains("set"));
			Assert.assertTrue(anExc.getMessage().contains("add"));
			Assert.assertTrue(anExc.getMessage().contains("put"));
		}
	}
	
	@Test
	public void dynamicImplementationsProduceReasonableToString() {
		DocumentReader<Root> _r = newCorrectRootUnmarshaller();
		Root _root = _r.read(get("root-5.xml"), "utf-8");
		
System.out.println(_root.toString());		
		
		Assert.assertTrue(_root.toString().startsWith("proxy(" + Root.class.getName() + "){"));		
		Assert.assertTrue(_root.toString().contains("Attr1=first"));
		Assert.assertTrue(_root.toString().contains("Attr2=second"));
		Assert.assertTrue(_root.toString().contains("Elem1=one"));
		Assert.assertTrue(_root.toString().contains("Elem2=two"));
	}
	
	@Test
	public void dynamicImplementationsCanIncorporateListsOfRepeatedElements() {
		DocumentReader<Profile1> _r = newProfile1Unmarshaller();
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
		DocumentReader<Profile2> _r = newProfile2Unmarshaller();
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
		DocumentReader<Person> _r = newPersonUnmarshaller();
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
