package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.DocumentReader.*;

import java.io.*;
import java.text.*;
import java.util.*;

import org.junit.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.convert.*;

public class TwitterFeedTest {

	// This creates a new reader capable of parsing twitter responses.
	// Only call this once, then re-use the returned DocumentReader object (it is thread safe!)
	private static DocumentReader<Tweets> newReader() {
		DocumentReader<Tweets> _tweetsReader = mappingOf(Tweets.class).to(
			tag("entry", Tweet.class).with(
				tag("published"),
				tag("title"),
				tag("content", Content.class).with(
					attribute("type"),
					pcdataMappedTo("value")
				),
				tag("author", Author.class).with(
					tag("name"),
					tag("uri")
				)
			)
		);
		
		_tweetsReader.registerConverters(new ThreadSafeDateConverter("yyyy-MM-dd'T'HH:mm:ss"));
		
		return _tweetsReader;
	}
	
	@Test
	public void marshallsTwitterResponseToTweetObjects() throws Exception {
		Tweets _tweets = marshallTestDocumentToTweets();
		
		Assert.assertEquals(15, _tweets.size());
		
		Tweet _first = _tweets.get(0);
		
		Assert.assertEquals(
			"Note de lecture : Succeeding with Use Cases par Richard Denney" +
			" http://t.co/5lcCXWsO #bookReview #useCases #UML",
			_first.getTitle()
		);
		
		DateFormat _df = new SimpleDateFormat("yyyyMMddHHmmss");
		Assert.assertEquals(_df.parse("20120409101024"), _first.getPublished());
		
		Assert.assertEquals("html", _first.getContent().getType());
		
		Assert.assertEquals(
			"Note de lecture : Succeeding with Use Cases par Richard Denney <a href=" + 
		    "\"http://t.co/5lcCXWsO\">http://t.co/5lcCXWsO</a> <a href=\"http://" +
			"search.twitter.com/search?q=%23bookReview\" title=\"#bookReview\"" +
		    " class=\" \">#bookReview</a> <a href=\"http://search.twitter.com/search?" +
			"q=%23useCases\" title=\"#useCases\" class=\" \">#useCases</a> <em><a " +
		    "href=\"http://search.twitter.com/search?q=%23UML\" title=\"#UML\" class=\" \"" +
			">#UML</a></em>", 
			_first.getContent().getValue()
		);
		
		Assert.assertEquals(
			"addinquy (Christophe Addinquy)",
			_first.getAuthor().getName()
		);
		
		Assert.assertEquals(
			"http://twitter.com/addinquy",
			_first.getAuthor().getUri()
		);
		
	}
	
	private Tweets marshallTestDocumentToTweets() {
		DocumentReader<Tweets> _m = newReader();
		return _m.read(getTestInput(), "utf-8");
	}

	private InputStream getTestInput() {
		return getClass().getResourceAsStream("example4.xml");
	}
	
	
// model classes	
	
	
	public static class Tweets implements Iterable<Tweet> {
		private List<Tweet> tweets;
		
		public Tweets() {
			tweets = new ArrayList<Tweet>();
		}
		
		public void addTweet(Tweet aTweet) {			
			tweets.add(aTweet);
		}
		
		public Iterator<Tweet> iterator() {
			return tweets.iterator();
		}
		
		public int size() {
			return tweets.size();
		}
		
		public Tweet get(int anIndex) {
			return tweets.get(anIndex);
		}
	}
	
	public static class Tweet {
		public String title;
		public Date published;
		public Content content;
		private Language language;
		private Author author;
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String aTitle) {
			title = aTitle;
		}
		
		public Date getPublished() {
			return published;
		}
		
		public void setPublished(Date aDate) {
			published = aDate;
		}
		
		public Content getContent() {
			return content;
		}
		
		public void setContent(Content aContent) {
			content = aContent;
		}
		
		public Language getLanguage() {
			return language;
		}
		
		public void setLanguage(Language aLanguage) {
			language = aLanguage;
		}
		
		public Author getAuthor() {
			return author;
		}
		
		public void setAuthor(Author aAuthor) {
			author = aAuthor;
		}
	}
	

	public static class Language {
		public String code;
		
		public Language() {}
		
		public String getCode() {
			return code;
		}
		
		public void setCode(String aCode) {
			code = aCode;
		}
	}
	
	public static class Content {
		public String type;
		public String value;
		
		public String getType() {
			return type;
		}
		
		public void setType(String aType) {
			type = aType;			
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String aValue) {
			value = aValue;		
		}
	}
	
	public static class Author {
		private String name;
		private String uri;
		
		public Author() {}
		
		public String getName() {
			return name;
		}
		
		public void setName(String aName) {
			name = aName;
		}
		
		public String getUri() {
			return uri;
		}
		
		public void setUri(String aUri) {
			uri = aUri;
		}
	}
}
