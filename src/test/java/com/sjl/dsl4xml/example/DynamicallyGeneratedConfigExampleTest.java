package com.sjl.dsl4xml.example;

import static com.sjl.dsl4xml.SAXDocumentReader.*;

import java.io.*;

import org.junit.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.example.DynamicallyGeneratedConfigExampleTest.Config.*;

public class DynamicallyGeneratedConfigExampleTest {

	@Test
	public void mapsConfigurationToDynamicallyCreatedImplementationsOfInterfaces()
	throws Exception {
		DocumentReader<Config> _p = newMarshaller();
		Config _c = _p.read(getTestInput(), "utf-8");
		
		Assert.assertNotNull(_c);
		
		Database _db = _c.getDatabase();
		
		Assert.assertNotNull(_db);
		Assert.assertEquals("my-db", _db.getName());
		
	}
	
	private DocumentReader<Config> newMarshaller() {
		return mappingOf("config", Config.class).to(
			tag("database", Config.Database.class).with(
				attributes("name"),
				tag("host", Config.Host.class).with(
					tag("name"), tag("port")
				),
				tag("credentials", Config.Credentials.class).with(
					tag("username"), tag("password")
				)
			));
	}

	private InputStream getTestInput() {
		return getClass().getResourceAsStream("config-example.xml");
	}
	
	public interface HasName {
		public String getName();
		public void setName(String aName);
	}
	
	public interface Config extends HasName {
		
		public interface Host extends HasName {
			public Integer getPort();
			public void setPort(Integer aPort);
		}
		
		public interface Credentials {
			public String getUsername();
			public void setUsername(String aUsername);
			
			public String getPassword();
			public void setPassword(String aPassword);
		}
		
		public interface Database extends HasName {
			public Host getHost();
			public void setHost(Host aHost);
			
			public Credentials getCredentials();
			public void setCredentials(Credentials aCredentials);
		}
		
		public Database getDatabase();
		public void setDatabase(Database aDatabase);
	}
	
}
