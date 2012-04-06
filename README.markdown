# Easy and fast marshalling of XML to Java

DOM parsing tends to make for code that is easy to read and write, but is very slow, memory intensive, and generates heaps of garbage. 

SAX and "pull" parsing tend to be very fast, have significantly lower memory requirements, and typically produce much less garbage, but can lead to complex code and tortuously nested `if` statements, or lots of boiler-plate code to create state-machines.

Inspired by some recent work speeding up XML parsing in a slow Android application, `dsl4xml` is an experiment with the following aims:

1. To make _readable_, maintainable, declarative code that marshalls XML documents to Java objects.
2. To make marshalling XML documents to Java objects very fast (near pull-parsing speeds).
3. To avoid polluting model classes with metadata about xml parsing (no annotations).
4. To avoid additional build-time steps (code generators, etc).

It works by providing a thin DSL wrapper around a Pull-Parser to declaratively construct a state-machine that marshalls XML documents into Java objects. 

The DSL mirrors the structure of the XML document itself, making it very easy to write (and importantly to _read_ and _maintain_) XML marshalling code.

Boiler-plate is minimised through use of reflection, which does of course incur some performance penalty. The penalty is reduced where possible by caching reflectively gleaned information.

## Examples

### Simple XML, no attributes

Given a simple XML like this:

	<books>
	    <book>
	        <title>The Hobbit</title>
	        <synopsis>A little guy goes on an adventure, finds ring, comes back.</synopsis>
	    </book>
	    <book>
	        <title>The Lord of the Rings</title>
	        <synopsis>A couple of little guys go on an adventure, lose ring, come back.</synopsis>
	    </book>
	</books>

And some simple model objects we want to marshall to:

    class Books implements Iterable<Book> {
       private List<Book> books = new ArrayList<Book>();
       
       public void addBook(Book aBook) {
           books.add(aBook)l
       }
       
       public Iterator<Book> iterator() {
           return books.iterator();
       }
    }
    
    class Book {
    	private String title;
    	private String synopsis;
    	
    	public String getTitle() {
    	    return title;
    	}
    	
    	public void setTitle(String aTitle) {
    	    title = aTitle;
    	}
    	
    	public String getSynopsis() {
    	    return synopsis;
    	}
    	
    	public void setSynopsis(String aSynopsis) {
    	    synopsis = aSynopsis;
    	}
    }

We can marshall the XML to those model objects using the following simple Java code:

    import static com.sjl.dsl4xml.DocumentMarshaller.*;

    class BooksMarshaller {
	    private DocumentMarshaller<Books> marshaller;

	    public BooksMarshaller() {
	        marshaller = mappingOf(Books.class).to(
		        tag("book", Book.class).with(
	               tag("title").withCData(),
	               tag("synopsis").withCData()
			    )
		    );
	    }

        public Books marshall(Reader aReader) {
            return marshaller.marshall(aReader);
	    }
	}
	
### Simple XML with attributes

XML:

	<example>
	    <hobbit firstname="frodo" surname="baggins" age="50"/>
	    <hobbit firstname="samwise" surname="gamgee" age="35"/>
	    <hobbit firstname="peregrine" surname="took"/>
	    <hobbit firstname="meriadoc" age="32"/>
	</example>
	
POJO's:

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
		
		public int getAge() {
			return age;
		}
		
		public void setAge(int aAge) {
			age = aAge;
		}
	}
	
Marshalling code:

	import static com.sjl.dsl4xml.DocumentMarshaller.*;

    class HobbitsMarshaller {
	    private DocumentMarshaller<Hobbits> marshaller;

	    public HobbitsMarshaller() {
	        marshaller = mappingOf(Hobbits.class).to(
		        tag("hobbit", Hobbit.class).with(
	               attributes("firstname", "surname", "age")
			    )
		    );
	    }

        public Hobbits marshall(Reader aReader) {
            return marshaller.marshall(aReader);
	    }
	}
	
### Deeper tag nesting, and type conversion

XML:

	<hobbit>
	  <name firstname="Frodo" surname="Baggins"/>
	  <dob>11400930</dob>
	  <address>
	    <house>
		  <name>Bag End</name>
		  <number></number>
		</house>
	 	<street>Bagshot Row</street>
	 	<town>Hobbiton</town>
	 	<country>The Shire</country>
	  </address>
	</hobbit>
	
POJO's: [See the source-code of the test-case](https://github.com/steveliles/dsl4xml/commit/ad2141df218a776ebd68a75072feab16a5221fd5#diff-4)
	
Marshalling code:

	private static DocumentMarshaller<Hobbit> newMarshaller() {
		DocumentMarshaller<Hobbit> _marshaller = mappingOf(Hobbit.class).to(
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
		
		_marshaller.registerConverters(new UnsafeDateConverter("yyyyMMdd"));
		
		return _marshaller;
	}
	
<script 
  type="text/javascript" language="javascript" 
  src="http://steveliles.github.com/blog/blog.nocache.js">
</script>