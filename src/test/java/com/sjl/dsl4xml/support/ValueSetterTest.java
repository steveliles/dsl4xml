package com.sjl.dsl4xml.support;

import org.jmock.*;
import org.junit.*;

import com.sjl.dsl4xml.*;

public class ValueSetterTest {

	private HasConverters converters;
	private TestModel model;
	private Converter<Integer> integerConverter;
	private Mockery mockery;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		mockery = new Mockery();
		converters = mockery.mock(HasConverters.class);
		model = mockery.mock(TestModel.class);
		integerConverter = mockery.mock(Converter.class);
	}
	
	@After
	public void teardown() {
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void usesSuppliedConvertersToPerformTypeConversion() {
		mockery.checking(new Expectations() {{
			oneOf(converters).getConverter(Integer.class); // parameter to the setFieldOne method is of type Integer
		}});
		
		new ValueSetter(converters, TestModel.class, "fieldOne");
	}

	@Test
	public void introspectsModelClassToFindAndInvokeSetterMethods() {
		mockery.checking(new Expectations() {{
			oneOf(converters).getConverter(Integer.class); // parameter to the setFieldOne method is of type Integer
			will(returnValue(integerConverter));
			
			oneOf(integerConverter).convert("53");
			will(returnValue(new Integer(53)));
			
			oneOf(model).setFieldOne(new Integer(53));
		}});
		
		ValueSetter _s = new ValueSetter(converters, TestModel.class, "fieldOne");
		_s.invoke(model, "53");
	}
	
	@Test
	public void introspectsModelClassToFindAndInvokeAddMethods() {
		mockery.checking(new Expectations() {{
			oneOf(converters).getConverter(Integer.class); // parameter to the setFieldOne method is of type Integer
			will(returnValue(integerConverter));
			
			oneOf(integerConverter).convert("17");
			will(returnValue(new Integer(17)));
			
			oneOf(model).addFieldTwo(new Integer(17));
		}});
		
		ValueSetter _s = new ValueSetter(converters, TestModel.class, "fieldTwo");
		_s.invoke(model, "17");
	}
	
	@Test
	public void introspectsModelClassToFindAndInvokeInsertMethods() {
		mockery.checking(new Expectations() {{
			oneOf(converters).getConverter(Integer.class); // parameter to the setFieldOne method is of type Integer
			will(returnValue(integerConverter));
			
			oneOf(integerConverter).convert("98");
			will(returnValue(new Integer(98)));
			
			oneOf(model).insertFieldThree(new Integer(98));
		}});
		
		ValueSetter _s = new ValueSetter(converters, TestModel.class, "fieldThree");
		_s.invoke(model, "98");
	}
	
	@Test
	public void throwsGoodExceptionWhenNoAvailableSetterMethod() {
		try {
			new ValueSetter(converters, TestModel.class, "fieldSix");
			Assert.fail("Expected an exception");
		} catch (NoSuitableMethodException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsGoodExceptionWhenNoArgSetterMethod() {
		try {
			new ValueSetter(converters, TestModel.class, "fieldFour");
			Assert.fail("Expected an exception");
		} catch (NoSuitableMethodException anExc) {
			// good
		}
	}
	
	@Test
	public void throwsGoodExceptionWhenMultiArgSetterMethod() {
		try {
			new ValueSetter(converters, TestModel.class, "fieldFive");
			Assert.fail("Expected an exception");
		} catch (NoSuitableMethodException anExc) {
			// good
		}
	}
	
	public interface TestModel {
		public void setFieldOne(Integer aValue);
		public void addFieldTwo(Integer aValue);
		public void insertFieldThree(Integer aValue);
		
		public void setFieldFour(); // not usable
		public void setFieldFive(Integer aParam1, Integer aParam2); // not usable
	}
	
}
