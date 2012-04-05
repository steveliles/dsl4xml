# Easy and fast marshalling of XML to Java

DOM parsing tends to make for code that is easy to read and write, but very slow and memory intensive. SAX and "pull" parsing lead to complex code, tortuously nested `if` statements, or developers writing state-machines to parse even simple documents.

`dsl4xml` is my free-time thought-experiment, with the following aims:

1. To make _readable_ declarative code that marshalls XML documents to Java objects.
2. To make marshalling XML documents to Java objects very fast (pull parsing speeds).
3. To avoid polluting model classes with metadata about xml parsing (no annotations).
4. To avoid additional build-time steps (code generators, etc).

It works by providing a thin DSL wrapper around a Pull-Parser to declaratively construct a state-machine that marshalls XML documents into Java objects. 

The DSL mirrors the structure of the XML document itself, making it very easy to write (and importantly to _read_ and _maintain_) XML marshalling code.

## Examples

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

And some simple model objects we want to marshall to that look like this:

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

We can marshall the XML to those model objects using the following simple declarative Java code:

    import static com.sjl.dsl4xml.DocumentMapper.*;

    class BooksMarshaller {
	    private DocumentMapper<Books> mapper;

	    public BooksMarshaller() {
	        mapper = mappingOf(Books.class).with(
		        tag("book", Book.class).with(
	               tag("title").withCData(),
	               tag("synopsis").withCData()
			    )
		    );
	    }

        public Books marshall(Reader aReader) {
            return mapper.map(aReader);
	    }
	}
	
.. more soon ...