package com.sjl.dsl4xml.support;

import org.jmock.*;
import org.junit.*;

import com.sjl.dsl4xml.*;

public class ValueSetterTest {

	private HasConverters converters;
	
	private Mockery mockery;
	
	@Before
	public void setup() {
		mockery = new Mockery();
		converters = mockery.mock(HasConverters.class);
	}
	
	@Test
	public void testUsesSuppliedConvertersToPerformTypeConversion() {
		
		
		ValueSetter _s = new ValueSetter(converters, String.class, "fieldname");
	}
	
}
