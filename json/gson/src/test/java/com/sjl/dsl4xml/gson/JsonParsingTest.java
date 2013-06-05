package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.Converter;
import com.sjl.dsl4xml.json.JsonDocumentDefinition;
import com.sjl.dsl4xml.support.Factory;
import com.sjl.dsl4xml.support.convert.ThreadSafeDateStringConverter;
import junit.framework.Assert;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author steve
 */
public class JsonParsingTest
{
	interface Member1 {
		interface Person {
			public String getId();
			public String getTitle();
			public String getFirstname();
			public String getLastname();
			public String getEmail();
		}
		interface Social {
			public String getProviderId();
			public String getProviderUserId();
			public String getImageUrl();
		}

		public String getId();
		public Date getRegistrationDate();
		public Person getPerson();
		public Social getSocial();
		public int getPointsAccrued();
	}

	@Test
	public void parsesComplexMemberJson() throws Exception {
		GsonDocumentReader<Member1> _reader = new GsonDocumentReader<Member1>(
            new JsonDocumentDefinition<Member1>(){{
                registerConverters(new ThreadSafeDateStringConverter("yyyy-MM-dd"));

                mapping(Member1.class).with(
                    property("id"),
                    property("registrationDate"),
                    object("person", Member1.Person.class).with(
                        property("id"),
                        property("firstname"),
                        property("lastname"),
                        property("email"),
                        property("title")
                    ),
                    object("social", Member1.Social.class).with(
                        property("providerId"),
                        property("providerUserId"),
                        property("imageUrl")
                    ),
                    number("pointsAccrued", Integer.class)
                );
        }});

		Member1 _result = _reader.read(new InputStreamReader(getClass().getResourceAsStream("member-1.json")));

		Assert.assertNotNull(_result);
		Assert.assertEquals("5e39dcc6-d4e3-5067-0058-aec52c70f0d3", _result.getId());
		Assert.assertEquals(new SimpleDateFormat("yyyyMMdd").parse("20130501"), _result.getRegistrationDate());

		Member1.Person _person = _result.getPerson();
		Assert.assertNotNull(_person);
		Assert.assertEquals("5e39dcc6-d4e3-5067-0058-aec52c70f0d3", _person.getId());
		Assert.assertEquals("Steve", _person.getFirstname());
		Assert.assertEquals("Liles", _person.getLastname());
		Assert.assertNull(_person.getEmail());
		Assert.assertNull(_person.getTitle());

		Member1.Social _social = _result.getSocial();
		Assert.assertNotNull(_social);
		Assert.assertEquals("twitter", _social.getProviderId());
		Assert.assertEquals("xxxxxxxx", _social.getProviderUserId());
		Assert.assertEquals("http://a0.twimg.com/profile_images/1635413135/viking-8_normal.png", _social.getImageUrl());

		Assert.assertEquals(50, _result.getPointsAccrued());
	}

	interface Member2 {
		interface Identifier {
			public String toSerializedForm();
		}
		interface Person {
			public Identifier getId();
			public String getTitle();
			public String getFirstname();
			public String getLastname();
			public String getEmail();
		}
		interface Social {
			public String getProviderId();
			public String getProviderUserId();
			public String getImageUrl();
		}

		public Identifier getId();
		public Date getRegistrationDate();
		public Person getPerson();
		public Social getSocial();
		public int getPointsAccrued();
	}

	final static class ImmutableIdentifier implements Member2.Identifier {
		public static ImmutableIdentifier create(Member2.Identifier aFrom) {
			return new ImmutableIdentifier(aFrom.toSerializedForm());
		}

		private final String value;

		private ImmutableIdentifier(String aValue) {
			value = aValue;
		}

		@Override
		public String toSerializedForm() {
			return value;
		}
	}

	final static class ImmutableIdentifierFactory
	implements Factory<Member2.Identifier, ImmutableIdentifier>
	{
		@Override
		public ImmutableIdentifier newTarget(Member2.Identifier anIntermediary) {
			return ImmutableIdentifier.create(anIntermediary);
		}
	}

	@Test
	public void parsesComplexMemberJsonWithIdObjects() throws Exception {
        GsonDocumentReader<Member2> _reader = new GsonDocumentReader<Member2>(
            new JsonDocumentDefinition<Member2>(){{
                registerConverters(new ThreadSafeDateStringConverter("yyyy-MM-dd"));

                mapping(Member2.class).with(
                    object("id", Member2.Identifier.class).with(
                        property("serializedForm")
                    ),
                    property("registrationDate"),
                    object("person", Member2.Person.class).with(
                        object("id", Member2.Identifier.class).with(
                            property("serializedForm")
                        ),
                        property("firstname"),
                        property("lastname"),
                        property("email"),
                        property("title")
                    ),
                    object("social", Member2.Social.class).with(
                        property("providerId"),
                        property("providerUserId"),
                        property("imageUrl")
                    ),
                    number("pointsAccrued", Integer.class)
                );
            }});


		Member2 _result = _reader.read(new InputStreamReader(getClass().getResourceAsStream("member-2.json")));

		Assert.assertNotNull(_result);
		Assert.assertEquals("5e39dcc6-d4e3-5067-0058-aec52c70f0d3", _result.getId().toSerializedForm());
		Assert.assertEquals(new SimpleDateFormat("yyyyMMdd").parse("20130501"), _result.getRegistrationDate());

		Member2.Person _person = _result.getPerson();
		Assert.assertNotNull(_person);
		Assert.assertEquals("5e39dcc6-d4e3-5067-0058-aec52c70f0d3", _person.getId().toSerializedForm());
		Assert.assertEquals("Steve", _person.getFirstname());
		Assert.assertEquals("Liles", _person.getLastname());
		Assert.assertNull(_person.getEmail());
		Assert.assertNull(_person.getTitle());

		Member2.Social _social = _result.getSocial();
		Assert.assertNotNull(_social);
		Assert.assertEquals("twitter", _social.getProviderId());
		Assert.assertEquals("xxxxxxxx", _social.getProviderUserId());
		Assert.assertEquals("http://a0.twimg.com/profile_images/1635413135/viking-8_normal.png", _social.getImageUrl());

		Assert.assertEquals(50, _result.getPointsAccrued());
	}

	@Test
	public void parsesComplexMemberJsonToImmutableObjects() throws Exception {
		GsonDocumentReader<Member2> _reader = createMember2Reader();

		Member2 _result = _reader.read(new InputStreamReader(getClass().getResourceAsStream("member-2.json")));

		Assert.assertNotNull(_result);
        Assert.assertTrue(_result.getId() instanceof ImmutableIdentifier);
		Assert.assertEquals("5e39dcc6-d4e3-5067-0058-aec52c70f0d3", _result.getId().toSerializedForm());
		Assert.assertEquals(new SimpleDateFormat("yyyyMMdd").parse("20130501"), _result.getRegistrationDate());

		Member2.Person _person = _result.getPerson();
		Assert.assertNotNull(_person);
		Assert.assertTrue(_person.getId() instanceof ImmutableIdentifier);
		Assert.assertEquals("5e39dcc6-d4e3-5067-0058-aec52c70f0d3", _person.getId().toSerializedForm());
		Assert.assertEquals("Steve", _person.getFirstname());
		Assert.assertEquals("Liles", _person.getLastname());
		Assert.assertNull(_person.getEmail());
		Assert.assertNull(_person.getTitle());

		Member2.Social _social = _result.getSocial();
		Assert.assertNotNull(_social);
		Assert.assertEquals("twitter", _social.getProviderId());
		Assert.assertEquals("xxxxxxxx", _social.getProviderUserId());
		Assert.assertEquals("http://a0.twimg.com/profile_images/1635413135/viking-8_normal.png", _social.getImageUrl());

		Assert.assertEquals(50, _result.getPointsAccrued());
	}

	interface ThingWithText {
		public List<String> asText();
	}

	@Test
	public void parsesArraysOfLengthOne() {
		GsonDocumentReader<ThingWithText> _reader = new GsonDocumentReader<ThingWithText>(
            new JsonDocumentDefinition<ThingWithText>() {{
                mapping(ThingWithText.class).with(
                    array("text", List.class).of(
                        property(String.class)
                    ));
            }}
        );

		ThingWithText _twt = _reader.read(new StringReader("{\"text\":[\"one\"]}"));

		Assert.assertNotNull(_twt);
		Assert.assertNotNull(_twt.asText());
		Assert.assertEquals(1, _twt.asText().size());
		Assert.assertEquals("one", _twt.asText().get(0));
	}

	@Test
	public void parsesArraysOfStrings() {
		GsonDocumentReader<ThingWithText> _reader = new GsonDocumentReader<ThingWithText>(
            new JsonDocumentDefinition<ThingWithText>() {{
                mapping(ThingWithText.class).with(
                    array("text", List.class).of(
                        property(String.class)
                    ));
            }}
		);

		ThingWithText _twt = _reader.read(new StringReader("{\"text\":[\"first\", \"second\", \"third\"]}"));

		Assert.assertNotNull(_twt);
		Assert.assertNotNull(_twt.asText());
		Assert.assertEquals(3, _twt.asText().size());
		Assert.assertEquals("first", _twt.asText().get(0));
		Assert.assertEquals("second", _twt.asText().get(1));
		Assert.assertEquals("third", _twt.asText().get(2));
	}

	public interface Truthy {
		public List<Boolean> getTruths();
	}

	@Test
	public void parsesArraysOfBooleans()
	{
		GsonDocumentReader<Truthy> _reader =  new GsonDocumentReader<Truthy>(
            new JsonDocumentDefinition<Truthy>() {{
                mapping(Truthy.class).with(
                        array("truths", List.class).of(
                                bool(Boolean.class)
                        ));
            }}
		);

		Truthy _t = _reader.read(new StringReader("{\"truths\":[true, false, true]}"));

		Assert.assertNotNull(_t);
		Assert.assertNotNull(_t.getTruths());
		Assert.assertEquals(3, _t.getTruths().size());
		Assert.assertTrue(_t.getTruths().get(0));
		Assert.assertFalse(_t.getTruths().get(1));
		Assert.assertTrue(_t.getTruths().get(2));
	}

	interface Numbers {
		public List<Float> getNumbers();
	}

	@Test
	public void parsesArraysOfNumbers()
	{
		GsonDocumentReader<Numbers> _reader =  new GsonDocumentReader<Numbers>(
            new JsonDocumentDefinition<Numbers>() {{
                mapping(Numbers.class).with(
                        array("numbers", List.class).of(
                                number(Float.class)
                        ));
            }}
		);

		Numbers _t = _reader.read(new StringReader("{\"numbers\":[1.0, 2.0, 3.0]}"));

		Assert.assertNotNull(_t);
		Assert.assertNotNull(_t.getNumbers());
		Assert.assertEquals(3, _t.getNumbers().size());
		Assert.assertEquals(1.0f, _t.getNumbers().get(0));
		Assert.assertEquals(2.0f, _t.getNumbers().get(1));
		Assert.assertEquals(3.0f, _t.getNumbers().get(2));
	}

	interface Dates {
		public List<Date> getDates();
	}

	@Test
	public void parsesArraysOfConvertedStrings() {
		GsonDocumentReader<Dates> _reader = new GsonDocumentReader<Dates>(
            new JsonDocumentDefinition<Dates>() {{
                registerConverters(new ThreadSafeDateStringConverter("yyyyMMddZ"));
                mapping(Dates.class).with(
                    array("dates", List.class).of(
                        property(Date.class)
                    )
                );
            }}
        );

		Dates _t = _reader.read(new StringReader("{\"dates\":[\"20130507+0000\", \"20130508+0000\", \"20130509+0000\"]}"));

		Assert.assertNotNull(_t);
		Assert.assertNotNull(_t.getDates());
		Assert.assertEquals(3, _t.getDates().size());
		Assert.assertEquals(new Date(1367884800000L), _t.getDates().get(0));
		Assert.assertEquals(new Date(1367971200000L), _t.getDates().get(1));
		Assert.assertEquals(new Date(1368057600000L), _t.getDates().get(2));
	}

	@Test
	public void skipsMissingObjects() {
		GsonDocumentReader<Member2> _reader = createMember2Reader();

		Member2 _result = _reader.read(new StringReader("{\"id\":{\"serializedForm\":\"1234\"}, \"pointsAccrued\":50}"));

		Assert.assertNotNull(_result);
		Assert.assertEquals("1234", _result.getId().toSerializedForm());
		Assert.assertNull(_result.getRegistrationDate());
		Assert.assertNull(_result.getPerson());
		Assert.assertEquals(50, _result.getPointsAccrued());
	}

	private GsonDocumentReader<Member2> createMember2Reader() {
        return new GsonDocumentReader<Member2>(
            new JsonDocumentDefinition<Member2>(){{

                registerConverters(new ThreadSafeDateStringConverter("yyyy-MM-dd"));

                converting(Member2.Identifier.class).to(ImmutableIdentifier.class).using(
                    new Converter<Member2.Identifier, ImmutableIdentifier>() {
                        public ImmutableIdentifier convert(Member2.Identifier aFrom) {
                            return ImmutableIdentifier.create(aFrom);
                        }
                    });

                mapping(Member2.class).with(
                    object("id", ImmutableIdentifier.class)
                        .via(Member2.Identifier.class)
                        .with(
                            property("serializedForm")
                        ),
                    property("registrationDate"),
                    object("person", Member2.Person.class).with(
                        object("id", ImmutableIdentifier.class)
                            .via(Member2.Identifier.class)
                            .with(
                                property("serializedForm")
                            ),
                        property("firstname"),
                        property("lastname"),
                        property("email"),
                        property("title")
                    ),
                    object("social", Member2.Social.class).with(
                        property("providerId"),
                        property("providerUserId"),
                        property("imageUrl")
                    ),
                    number("pointsAccrued", Integer.class)
                );
            }}
        );
	}

}
