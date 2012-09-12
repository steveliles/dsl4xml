# Easy and fast unmarshalling of XML to Java

DOM parsing tends to make for code that is easy to read and write, but is very slow, memory intensive, and generates heaps of garbage. 

SAX and "pull" parsing tend to be very fast, have significantly lower memory requirements, and typically produce much less garbage, but can lead to complex code and tortuously nested `if` statements, or lots of boiler-plate code to create state-machines.

JAXB and other xml-binding tools and frameworks can require dependencies on large libraries, or additional compile-time steps.

dsl4xml was inspired by some recent work speeding up and improving the readability of some complex (and _slow_) XML parsing code in an Android application.

### Aims

1. To make _readable_, maintainable, declarative code that unmarshalls XML documents to Java objects.
2. To make unmarshalling XML documents to Java objects very fast (sax/pull-parsing speeds).
3. To avoid polluting model classes with metadata about xml parsing (no annotations).
4. To avoid additional build-time steps (code generators, etc).
5. Very small jar and no additional dependencies for Android.

### How it works

dsl4xml works by providing a thin DSL wrapper around either a SAX or Pull-Parser to declaratively construct a state-machine that unmarshalls XML documents into Java objects. 

The DSL mirrors the structure of the XML document itself, making it very easy to write (and importantly to _read_ and _maintain_) XML unmarshalling code.

Boiler-plate is minimised through use of reflection, which does of course incur some performance penalty. The penalty is reduced where possible by caching reflectively gleaned information.

### Options for different platforms

Initially dsl4xml was written to work with a Pull-Parser only. Performance tests on desktop and laptop machines showed this to be consistently faster than raw SAX parsing.

Testing on Android devices, however, showed SAX parsing to be an order of magnitude faster than pull-parsing, so a second implementation of dsl4xml was created, wrapping a SAX parser.

If you are writing XML unmarshalling code for servers/desktops, use the Pull variant (statically import PullDocumentReader). When writing code to run on Android be very sure to statically import SAXDocumentReader - it makes a huge difference to performance!

Statically import `SAXDocumentReader` or `PullDocumentReader` to control which parsing method is used and to bring the dsl into scope (ie., so you can write it without prefixing).

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

We can unmarshall the XML to those model objects using the following simple Java code:

    import static com.sjl.dsl4xml.SAXDocumentReader.*;

    class BooksReader {
	    private DocumentReader<Books> reader;

	    public BooksReader() {
	        reader = mappingOf(Books.class).to(
		        tag("book", Book.class).with(
	               tag("title"),
	               tag("synopsis")
			    )
		    );
	    }

        public Books read(Reader aReader) {
            return reader.read(aReader);
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
	
Unmarshalling code:

	import static com.sjl.dsl4xml.SAXDocumentReader.*;

    class HobbitsReader {
	    private DocumentReader<Hobbits> Reader;

	    public HobbitsReader() {
	        reader = mappingOf(Hobbits.class).to(
		        tag("hobbit", Hobbit.class).with(
	               attributes("firstname", "surname", "age")
			    )
		    );
	    }

        public Hobbits read(Reader aReader) {
            return reader.read(aReader);
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
	
Unmarshalling code:

	import static com.sjl.dsl4xml.PullDocumentReader.*;

	private static DocumentReader<Hobbit> newReader() {
		DocumentReader<Hobbit> _reader = mappingOf(Hobbit.class).to(
			tag("name", Name.class).with(
				attributes("firstname", "surname")
			),
			tag("dob"),
			tag("address", Address.class).with(
				tag("house", Address.House.class).with(
					tag("name"),
					tag("number")
				),
				tag("street"),
				tag("town"),
				tag("country")
			)
		);
		
		_reader.registerConverters(new UnsafeDateConverter("yyyyMMdd"));
		
		return _reader;
	}