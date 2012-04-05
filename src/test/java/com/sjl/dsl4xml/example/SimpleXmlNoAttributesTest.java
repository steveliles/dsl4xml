package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.DocumentMarshaller.*;

import java.io.*;
import java.util.*;

import junit.framework.Assert;

import org.junit.Test;

import com.sjl.dsl4xml.*;

public class SimpleXmlNoAttributesTest {

	@Test
	public void mapsTitleCorrectlyFromXml()
	throws Exception {
		Description _d = marshallTestDocumentToDescription();
		Assert.assertEquals("First Example", _d.getTitle());
	}

	@Test
	public void returnsNonNullSummary()
	throws Exception {
		Description _d = marshallTestDocumentToDescription();
		Assert.assertNotNull(_d.getParagraphs());
	}
	
	@Test
	public void mapsCorrectNumberOfParagraphsToSummary()
	throws Exception {
		Description _d = marshallTestDocumentToDescription();
		Assert.assertEquals(2, _d.getParagraphs().size());
	}
	
	@Test
	public void mapsCorrectParagraphOrderToSummary()
	throws Exception {
		Description _d = marshallTestDocumentToDescription();
		Assert.assertEquals("First paragraph.", _d.getParagraphs().get(0));
		Assert.assertEquals("Second paragraph.", _d.getParagraphs().get(1));
	}

	private Description marshallTestDocumentToDescription() {
		DocumentMarshaller<Description> _m = newMarshaller();
		return _m.map(getTestInput(), "utf-8");
	}

	private InputStream getTestInput() {
		return getClass().getResourceAsStream("example1.xml");
	}
	
	/**
	 * @return a DocumentMarshaller that can map documents like example1.xml
	 * to the Description class declared below.
	 */
	private static DocumentMarshaller<Description> newMarshaller() {
		return mappingOf(Description.class).to(
			tag("description").with(
				tag("title").withCData(),
				tag("summary").with(
					tag("p").withCDataMappedTo("paragraph")
				)
			)
		);
	}
	
	public static class Description {
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
