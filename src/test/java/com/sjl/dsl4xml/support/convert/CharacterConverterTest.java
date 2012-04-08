package com.sjl.dsl4xml.support.convert;

import org.junit.*;

public class CharacterConverterTest {

	@Test
	public void canConvertCharacters() {
		CharacterConverter _c = new CharacterConverter();
		Assert.assertTrue(_c.canConvertTo(Character.class));
	}
	
	@Test
	public void convertsSingleCharacter() {
		CharacterConverter _c = new CharacterConverter();
		Assert.assertEquals(new Character('s'), _c.convert("s"));
	}
	
	@Test
	public void convertsFirstCharacterIfMultiple() {
		CharacterConverter _c = new CharacterConverter();
		Assert.assertEquals(new Character('s'), _c.convert("steve"));
	}
	
}
