package com.sjl.dsl4xml.example;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.sjl.dsl4xml.*;

import static com.sjl.dsl4xml.SAXLegacyDocumentReader.*;

public class SimpleXmlWithHyphenatedAttributeNamesTest {

	@Test
	public void mapsHobbitTagToHobbitObject()
	throws Exception {
		LegacyDocumentReader<Hobbits> _p = newMarshaller();
		Hobbits _h = _p.read(getTestInput(), "utf-8");
		Assert.assertEquals(4, _h.size());
		Assert.assertEquals("frodo", _h.get(0).getFirstName());
		Assert.assertEquals("baggins", _h.get(0).getLastName());
	}

	private InputStream getTestInput() {
		return getClass().getResourceAsStream("example5.xml");
	}
	
	private LegacyDocumentReader<Hobbits> newMarshaller() {
		return mappingOf("example", Hobbits.class).to(
			tag("hobbit", Hobbit.class).with(
				attributes("first-name", "last-name", "age")
			)
		);
	}

	public static class Hobbits {
		private List<Hobbit> hobbits;
		
		public Hobbits() {
			hobbits = new ArrayList<Hobbit>();
		}
		
		public void addHobbit(Hobbit aHobbit) {
			hobbits.add(aHobbit);
		}
		
		public int size() {
			return hobbits.size();
		}
		
		public Hobbit get(int anIndex) {
			return hobbits.get(anIndex);
		}
	}
	
	public static class Hobbit {
		private String firstname;
		private String surname;
		private int age;
		
		public String getFirstName() {
			return firstname;
		}
		
		public void setFirstName(String aFirstname) {
			firstname = aFirstname;
		}
		
		public String getLastName() {
			return surname;
		}
		
		public void setLastName(String aSurname) {
			surname = aSurname;
		}
		
		public int getAge() {
			return age;
		}
		
		public void setAge(int aAge) {
			age = aAge;
		}
	}
		
}
