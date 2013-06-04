package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.SAXLegacyDocumentReader.*;

import java.io.*;
import java.util.*;

import junit.framework.Assert;

import org.junit.Test;

import com.sjl.dsl4xml.*;

public class UnknownTagsTest {
	
	@Test
	public void processesEmptyInputCorrectly()
	throws Exception {
		MainTag mainTag = newMarshaller().read(new StringReader("<mainTag></mainTag>"));
		Assert.assertEquals(0, mainTag.size());
	}
	
	@Test
	public void processesUnknownSubTagCorrectly()
	throws Exception {
		MainTag mainTag = newMarshaller().read(new StringReader("<mainTag><unknownSubTag>foo</unknownSubTag></mainTag>"));
		Assert.assertEquals(0, mainTag.size());
	}
	
	@Test
	public void processesUnknownSubTagWithinTagWithNoTagHandlers()
	throws Exception {
		MainTag mainTag = newMarshaller().read(new StringReader("<mainTag><subTag><unknownSubTag>foo</unknownSubTag></subTag></mainTag>"));
		Assert.assertEquals(1, mainTag.size());
	}
	
	private static LegacyDocumentReader<MainTag> newMarshaller() {
		LegacyDocumentReader<MainTag> _result = mappingOf("mainTag", MainTag.class).to(
			tag("subTag", SubTag.class)
		);
		return _result;
	}

	public static class MainTag {
		private List<SubTag> subTags;
		
		public MainTag() {
			subTags = new ArrayList<SubTag>();
		}
		
		public void addSubTag(SubTag subTag) {
			subTags.add(subTag);
		}
		
		public int size() {
			return subTags.size();
		}
	}
	
	public static class SubTag {
		public SubTag() {}
	}
}
