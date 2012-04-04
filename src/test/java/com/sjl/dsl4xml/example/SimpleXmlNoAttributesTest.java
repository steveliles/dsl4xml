package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.DocumentMapper.*;

import java.io.*;
import java.util.*;

import junit.framework.Assert;

import org.junit.Test;

import com.sjl.dsl4xml.*;

public class SimpleXmlNoAttributesTest {

	@Test
	public void mapsTitleCorrectlyFromXml()
	throws Exception {
		Parser _p = new Parser().init();
		Description _d = _p.parse(getTestInput());
		Assert.assertEquals("First Example", _d.getTitle());
	}

	@Test
	public void returnsNonNullSummary()
	throws Exception {
		Parser _p = new Parser().init();
		Description _d = _p.parse(getTestInput());
		Assert.assertNotNull(_d.getParagraphs());
	}
	
	@Test
	public void mapsCorrectNumberOfParagraphsToSummary()
	throws Exception {
		Parser _p = new Parser().init();
		Description _d = _p.parse(getTestInput());
		Assert.assertEquals(2, _d.getParagraphs().size());
	}
	
	@Test
	public void mapsCorrectParagraphOrderToSummary()
	throws Exception {
		Parser _p = new Parser().init();
		Description _d = _p.parse(getTestInput());
		Assert.assertEquals("First paragraph.", _d.getParagraphs().get(0));
		Assert.assertEquals("Second paragraph.", _d.getParagraphs().get(1));
	}

	private InputStream getTestInput() {
		return getClass().getResourceAsStream("example1.xml");
	}
	
	private static class Parser extends AbstractParser<Description> {
		
		@Override
		protected Description newResultObject() {
			return new Description();
		}

		@Override
		protected DocumentMapper<Description> defineMapper() {
			return xmlMappingTo(Description.class).with(
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
	}
	
	static class Description {
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
