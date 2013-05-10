package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.SAXDocumentReader.*;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.sjl.dsl4xml.*;

public class SimpleXmlWithAttributesDynamicImplementationTest {
	
	@Test
	public void mapsHobbitsTagToHobbitsObject()
	throws Exception {
		DocumentReader<Hobbits> _p = newMarshaller();
		Hobbits _h = _p.read(getTestInput(), "utf-8");
		Assert.assertEquals(4, _h.size());
	}
	
	@Test
	public void mapsHobbitTagToHobbitObject()
	throws Exception {
		DocumentReader<Hobbits> _p = newMarshaller();
		Hobbits _h = _p.read(getTestInput(), "utf-8");
		
		Halfling _first = _h.get(0);
		
		Assert.assertEquals("frodo", _first.getFirstname());
		Assert.assertEquals("baggins", _first.getSurname());
		Assert.assertEquals(50, _first.getAge());
	}

	private InputStream getTestInput() {
		return getClass().getResourceAsStream("example2.xml");
	}
	
	private DocumentReader<Hobbits> newMarshaller() {
		return mappingOf("example", Hobbits.class).to(
			tag("hobbit", Halfling.class).with(
				attributes("firstname", "surname", "age")
			)
		);
	}

	public static interface Hobbits extends List<Halfling> {
	}
	
	public static interface Halfling {
		public String getFirstname();
		public void setFirstname(String aFirstname);
		
		public String getSurname();
		public void setSurname(String aSurname);
		
		public int getAge();
		public void setAge(int anAge);
	}
}
