package com.sjl.dsl4xml.gson;

import static com.sjl.dsl4xml.GsonDocumentReader.*;
import com.sjl.dsl4xml.GsonDocumentReader;
import com.sjl.dsl4xml.support.Factory;
import com.sjl.dsl4xml.support.IntermediaryProxyingFactory;
import com.sjl.dsl4xml.support.convert.ThreadSafeDateConverter;
import junit.framework.Assert;
import org.junit.Test;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	extends IntermediaryProxyingFactory<Member2.Identifier, ImmutableIdentifier>
	{
		public ImmutableIdentifierFactory() {
			super(Member2.Identifier.class);
		}

		@Override
		public ImmutableIdentifier newTarget(Member2.Identifier anIntermediary) {
			return ImmutableIdentifier.create(anIntermediary);
		}
	}

	@Test
	public void parsesComplexMemberJson() throws Exception {
		GsonDocumentReader<Member1> _reader = mappingOf(Member1.class).to(
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
			property("pointsAccrued")
		);

		_reader.registerConverters(new ThreadSafeDateConverter("yyyy-MM-dd"));

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

	@Test
	public void parsesComplexMemberJsonWithIdObjects() throws Exception {
		GsonDocumentReader<Member2> _reader = mappingOf(Member2.class).to(
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
			property("pointsAccrued")
		);

		_reader.registerConverters(new ThreadSafeDateConverter("yyyy-MM-dd"));

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
		GsonDocumentReader<Member2> _reader = mappingOf(Member2.class).to(
			object("id", new ImmutableIdentifierFactory()).with(
				property("serializedForm")
			),
			property("registrationDate"),
			object("person", Member2.Person.class).with(
				object("id", new ImmutableIdentifierFactory()).with(
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
			property("pointsAccrued")
		);

		_reader.registerConverters(new ThreadSafeDateConverter("yyyy-MM-dd"));

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
}
