package com.sjl.dsl4xml;

import org.jmock.*;
import org.junit.*;

import com.sjl.dsl4xml.pull.*;
import com.sjl.dsl4xml.support.*;

public class NamedAttributesReaderTest {

	private ReadingContext ctx;
	private TestModel model;
	private StringConverter<String> strConverter;
	private StringConverter<Integer> intConverter;
	
	private Mockery mockery;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		mockery = new Mockery();
		
		ctx = mockery.mock(ReadingContext.class);
		model = mockery.mock(TestModel.class);
		strConverter = (StringConverter<String>)mockery.mock(StringConverter.class, "string-converter");
		intConverter = (StringConverter<Integer>)mockery.mock(StringConverter.class, "int-converter");
	}
	
	@After
	public void tearDown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void readsSingleAttributeByName() {
		mockery.checking(new Expectations(){{
			oneOf(ctx).isStartTag(); will(returnValue(true));
			oneOf(ctx).getAttributeValue("stringAttr"); will(returnValue("value"));
			oneOf(ctx).getConverter(String.class); will(returnValue(strConverter));
			
			oneOf(ctx).peek(); will(returnValue(model));
			
			oneOf(strConverter).convert("value"); will(returnValue("value"));
			oneOf(model).setStringAttr("value");
		}});
		
		NamedAttributesReader _nar = new NamedAttributesReader("stringAttr");
		_nar.read(ctx);
	}
	
	@Test
	public void readsMultipleAttributesByName() {
		mockery.checking(new Expectations(){{
			oneOf(ctx).isStartTag(); will(returnValue(true));
			oneOf(ctx).getAttributeValue("stringAttr"); will(returnValue("value"));
			oneOf(ctx).getAttributeValue("intAttr"); will(returnValue("12"));
			oneOf(ctx).getConverter(String.class); will(returnValue(strConverter));
			oneOf(ctx).getConverter(Integer.TYPE); will(returnValue(intConverter));
			
			allowing(ctx).peek(); will(returnValue(model));
			
			oneOf(strConverter).convert("value"); will(returnValue("value"));
			oneOf(model).setStringAttr("value");
			
			oneOf(intConverter).convert("12"); will(returnValue(12));
			oneOf(model).setIntAttr(12);
		}});
		
		NamedAttributesReader _nar = new NamedAttributesReader("stringAttr", "intAttr");
		_nar.read(ctx);
	}
	
	interface TestModel {
		public void setStringAttr(String aValue);
		public void setIntAttr(int aValue);
	}
	
}
