package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class CharacterConverterTest {

	@Test
	public void canConvertCharacters() {
		CharacterStringConverter _c = new CharacterStringConverter();
		Assert.assertTrue(_c.canConvertTo(Character.class));
	}
	
	@Test
	public void convertsSingleCharacter() {
		CharacterStringConverter _c = new CharacterStringConverter();
		Assert.assertEquals(new Character('s'), _c.convert("s"));
	}
	
	@Test
	public void convertsFirstCharacterIfMultiple() {
		CharacterStringConverter _c = new CharacterStringConverter();
		Assert.assertEquals(new Character('s'), _c.convert("steve"));
	}
	
}
