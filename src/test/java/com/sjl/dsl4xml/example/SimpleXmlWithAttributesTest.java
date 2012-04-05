package com.sjl.dsl4xml.example;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.sjl.dsl4xml.*;

import static com.sjl.dsl4xml.DocumentMarshaller.*;

public class SimpleXmlWithAttributesTest {

	@Test
	public void mapsHobbitTagToHobbitObject()
	throws Exception {
		DocumentMarshaller<Hobbits> _p = newMarshaller();
		Hobbits _h = _p.map(getTestInput(), "utf-8");
		Assert.assertEquals(4, _h.size());
	}

	private InputStream getTestInput() {
		return getClass().getResourceAsStream("example2.xml");
	}
	
	private DocumentMarshaller<Hobbits> newMarshaller() {
		return mappingOf(Hobbits.class).to(
			tag("hobbit", Hobbit.class).with(
				attributes("firstname", "surname", "age")
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
		private String age;
		
		public String getFirstname() {
			return firstname;
		}
		
		public void setFirstname(String aFirstname) {
			firstname = aFirstname;
		}
		
		public String getSurname() {
			return surname;
		}
		
		public void setSurname(String aSurname) {
			surname = aSurname;
		}
		
		public String getAge() {
			return age;
		}
		
		public void setAge(String aAge) {
			age = aAge;
		}
	}
		
}
