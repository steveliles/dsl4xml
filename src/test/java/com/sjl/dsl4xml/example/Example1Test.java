package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.DocumentMapper.*;

import java.io.*;
import java.util.*;

import junit.framework.Assert;

import org.junit.Test;

import com.sjl.dsl4xml.*;

public class Example1Test {

	@Test
	public void mapsTitleCorrectlyFromXml()
	throws Exception {
		Example1Parser _p = new Example1Parser();
		Description _d = _p.parse(getClass().getResourceAsStream("example1.xml"));
		Assert.assertEquals("First Example", _d.getTitle());
	}
	
	@Test
	public void returnsNonNullSummary()
	throws Exception {
		Example1Parser _p = new Example1Parser();
		Description _d = _p.parse(getClass().getResourceAsStream("example1.xml"));
		Assert.assertNotNull(_d.getParagraphs());
	}
	
	@Test
	public void mapsCorrectNumberOfParagraphsToSummary()
	throws Exception {
		Example1Parser _p = new Example1Parser();
		Description _d = _p.parse(getClass().getResourceAsStream("example1.xml"));
		Assert.assertEquals(2, _d.getParagraphs().size());
	}
	
	@Test
	public void mapsCorrectParagraphOrderToSummary()
	throws Exception {
		Example1Parser _p = new Example1Parser();
		Description _d = _p.parse(getClass().getResourceAsStream("example1.xml"));
		Assert.assertEquals("First paragraph.", _d.getParagraphs().get(0));
		Assert.assertEquals("Second paragraph.", _d.getParagraphs().get(1));
	}
	
	private static class Example1Parser {
		private DocumentMapper<Description> mapper;
		
		public Example1Parser() {
			mapper = xmlMappingTo(Description.class).with(
				tag("description").with(
					tag("title").mappingCDataTo(title()), 
					tag("summary").with(
						tag("p").mappingCDataTo(paragraph())
					)
				)
			);
		}
		
		private Mapper title() {
			return new CDataMapper() {
				public void map(MappingContext aContext, String aCData) {					
					Description _d = (Description) aContext.getResult();
					_d.setTitle(aCData);
				}
			};
		}
		
		private Mapper paragraph() {
			return new CDataMapper() {
				public void map(MappingContext aContext, String aCData) {
					Description _d = (Description) aContext.getResult();
					_d.addParagraph(aCData);
				}
			};
		}
		
		public Description parse(InputStream anInputStream) {
			final Description _result = new Description();
			mapper.map(
				newUTF8Reader(anInputStream), 
				new MappingContext(_result)
			);
			return _result;
		}
		
		private Reader newUTF8Reader(InputStream anInputStream) {
			try {
				return new InputStreamReader(anInputStream, "utf-8");
			} catch (UnsupportedEncodingException anExc) {
				throw new RuntimeException(anExc);
			}
		}
	}
	
	private static class Description {
		private String title;
		private List<String> paragraphs;
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String aTitle) {
			title = aTitle;
		}
		
		public List<String> getParagraphs() {
			return paragraphs;
		}
		
		public void addParagraph(String aParagraph) {
			if (paragraphs == null) {
				paragraphs = new ArrayList<String>();
			}
			paragraphs.add(aParagraph);
		}
	}
	
}
