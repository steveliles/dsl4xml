package com.sjl.dsl4xml.gson;

import static com.sjl.dsl4xml.GsonDocumentReader.*;
import com.sjl.dsl4xml.GsonDocumentReader;
import com.sjl.dsl4xml.support.convert.ThreadSafeDateConverter;
import junit.framework.Assert;
import org.junit.Test;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author steve
 */
public class JsonParsingTest
{
	interface Member {
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
	public void parsesComplexMemberJson() {
		GsonDocumentReader<Member> _reader = mappingOf(Member.class).to(
			property("id"),
			property("registrationDate"),
			object("person", Member.Person.class).with(
				property("id"),
				property("firstname"),
				property("lastname"),
				property("email"),
				property("title")
			),
			object("social", Member.Social.class).with(
				property("providerId"),
				property("providerUserId"),
				property("imageUrl")
			),
			property("pointsAccrued")
		);

		_reader.registerConverters(new ThreadSafeDateConverter("yyyy-MM-dd"));

		Member _result = _reader.read(new InputStreamReader(getClass().getResourceAsStream("member.json")));

		Assert.assertNotNull(_result);
	}
}
