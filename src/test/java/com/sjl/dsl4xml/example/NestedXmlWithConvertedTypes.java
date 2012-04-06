package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.DocumentMarshaller.*;

import java.io.*;
import java.text.*;
import java.util.*;

import junit.framework.Assert;

import org.junit.Test;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.convert.*;

public class NestedXmlWithConvertedTypes {

	@Test
	public void mapsHobbitNameCorrectly()
	throws Exception {
		Hobbit _frodo = marshallTestDocumentToHobbit();
		Assert.assertEquals(new Name("Frodo", "Baggins"), _frodo.getName());
	}
	
	@Test
	public void mapsHobbitDateOfBirthCorrectly()
	throws Exception {
		Hobbit _frodo = marshallTestDocumentToHobbit();
		DateFormat _df = new SimpleDateFormat("yyyyMMdd");
		Assert.assertEquals(_df.parse("11400930"), _frodo.getDob());
	}
	
	@Test
	public void mapsHobbitHouseCorrectly()
	throws Exception {
		Hobbit _frodo = marshallTestDocumentToHobbit();
		Assert.assertEquals(new Address.House("Bag End"), _frodo.getAddress().getHouse());
	}
	
	@Test
	public void mapsHobbitAddressCorrectly()
	throws Exception {
		Hobbit _frodo = marshallTestDocumentToHobbit();
		
		Address _expectedAddress = new Address(
			new Address.House("Bag End"), "Bagshot Row", "Hobbiton", "The Shire"
		);
		
		Assert.assertEquals(_expectedAddress, _frodo.getAddress());
	}
	
	private Hobbit marshallTestDocumentToHobbit() {
		return newMarshaller().map(getTestInput(), "utf-8");
	}
	
	private InputStream getTestInput() {
		return getClass().getResourceAsStream("example3.xml");
	}
	
	/**
	 * @return a DocumentMarshaller that can map documents like example3.xml
	 * to the Hobbit class declared below. This includes nested Address fields
	 * with nested House fields, and fields that need type conversion (int's and Date's).
	 */
	private static DocumentMarshaller<Hobbit> newMarshaller() {
		DocumentMarshaller<Hobbit> _result = mappingOf(Hobbit.class).to(
			tag("name", Name.class).with(
				attributes("firstname", "surname")
			),
			tag("dob").withCData(),
			tag("address", Address.class).with(
				tag("house", Address.House.class).with(
					tag("name").withCData(),
					tag("number").withCData()
				),
				tag("street").withCData(),
				tag("town").withCData(),
				tag("country").withCData()
			)
		);
		
		_result.registerConverters(new ThreadUnsafeDateConverter("yyyyMMdd"));
		
		return _result;
	}

	public static class Hobbit {
		private Name name;
		private Address address;
		private Date dob;
		
		public Name getName() {
			return name;
		}
		
		public Address getAddress() {
			return address;
		}
		
		public Date getDob() {
			return dob;
		}
		
		public void setName(Name aName) {
			name = aName;
		}
		
		public void setAddress(Address aAddress) {
			address = aAddress;
		}
		
		public void setDob(Date aDob) {
			dob = aDob;
		}
	}
	
	public static class Name {
		private String firstname;
		private String surname;
		
		public Name(){}
		public Name(String aFirstname, String aSurname) {
			firstname = aFirstname;
			surname = aSurname;
		}
		
		public String getFirstname() {
			return firstname;
		}
		
		public String getSurname() {
			return surname;
		}
		
		public void setFirstname(String aFirstname) {
			firstname = aFirstname;
		}
		
		public void setSurname(String aSurname) {
			surname = aSurname;
		}
		
		public boolean equals(Object anObject) {
			if (anObject instanceof Name) {
				Name _other = (Name) anObject;
				return (
					((firstname != null) ? firstname.equals(_other.firstname) : (_other.firstname == null)) &&
					((surname != null) ? surname.equals(_other.surname) : (_other.surname == null))
				);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			int _hash = getClass().hashCode();
			if (firstname != null)
				_hash ^= firstname.hashCode();
			if (surname != null)
				_hash ^= surname.hashCode();
			return _hash;
		}
	}
	
	public static class Address {
		private House house;
		private String street;
		private String town;
		private String country;
		
		public Address(){}
		public Address(House aHouse, String aStreet, String aTown, String aCountry) {
			house = aHouse;
			street = aStreet;
			town = aTown;
			country = aCountry;
		}
		
		public House getHouse() {
			return house;
		}
		
		public String getStreet() {
			return street;
		}
		
		public String getTown() {
			return town;
		}
		
		public String getCountry() {
			return country;
		}
		
		public void setHouse(House aHouse) {
			house = aHouse;
		}
		
		public void setStreet(String aStreet) {
			street = aStreet;
		}
		
		public void setTown(String aTown) {
			town = aTown;
		}
		
		public void setCountry(String aCountry) {
			country = aCountry;
		}
		
		public boolean equals(Object anObject) {
			if (anObject instanceof Address) {
				Address _other = (Address) anObject;
				return (
					((house != null) ? house.equals(_other.house) : (_other.house == null)) &&
					((street != null) ? street.equals(_other.street) : (_other.street == null)) &&
					((town != null) ? town.equals(_other.town) : (_other.town == null)) &&
					((country != null) ? country.equals(_other.country) : (_other.country == null))
				);
			}
			return false;
		}
		
		public int hashCode() {
			int _hash = getClass().hashCode();
			
			if (house != null)
				_hash ^= house.hashCode();
			if (street != null)
				_hash ^= street.hashCode();
			if (town != null)
				_hash ^= town.hashCode();
			if (country != null)
				_hash ^= country.hashCode();
			
			return _hash;
		}
		
		public String toString() {
			StringBuilder _sb = new StringBuilder();
			
			if (house != null)
				_sb.append(house.toString()).append(", ");
			if (street != null)
				_sb.append(street.toString()).append(", ");
			if (town != null)
				_sb.append(town.toString()).append(", ");
			if (country != null)
				_sb.append(country.toString());
			
			return _sb.toString();
		}
		
		public static class House {
			private String name;
			private int number;
			
			public House(){}
			public House(String aName) { this(aName, 0); }
			public House(int aNumber) { this(null, aNumber); }
			public House(String aName, int aNumber) {
				name = aName;
				number = aNumber;
			}
			
			public String getName() {
				return name;
			}
			
			public int getNumber() {
				return number;
			}
			
			public void setName(String aName) {
				name = aName;
			}
			
			public void setNumber(int aNumber) {
				number = aNumber;
			}
			
			public boolean equals(Object anObject) {
				if (anObject instanceof House) {
					House _other = (House) anObject;
					return (
						((name != null) ? name.equals(_other.name) : (_other.name == null)) &&
						(number == _other.number)
					);
				}
				return false;
			}
			
			public int hashCode() {
				int _hash = getClass().hashCode();
				if (name != null)
					_hash ^= name.hashCode();
				_hash ^= number;
				return _hash;
			}
			
			public String toString() {
				if (name != null) {
					if (number > 0) {
						return name + ", no." + number;
					} else {
						return name;
					}
				} else {
					if (number > 0) {
						return number + "";
					} else {
						return "";
					}
				}
			}
		}
		
	}
	
}
