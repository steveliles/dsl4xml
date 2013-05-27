package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.support.DocumentReader2;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

public abstract class AbstractJsonParsingTest {

    protected abstract <T> DocumentReader2<T> newDocumentReader(DocumentDefinition<T> aDefinition);

    private JsonDocumentDefinitions defs = new JsonDocumentDefinitions();

    @Test
    public void testEmptyDocument(){
        DocumentReader2<Object> _r = newDocumentReader(defs.emptyDocument());
        Object _result = _r.read(newReader("{}"));
        Assert.assertNotNull(_result);
    }

    @Test
    public void testSimpleDocument() {
        DocumentReader2<JsonDocumentDefinitions.Simple> _r = newDocumentReader(defs.simpleProperty());
        JsonDocumentDefinitions.Simple _result = _r.read(newReader("{\"myProperty\":\"some value\"}"));
        Assert.assertNotNull(_result);
        Assert.assertEquals("some value", _result.getMyProperty());
    }

    @Test
    public void testJsonTypedProperties() {
        DocumentReader2<JsonDocumentDefinitions.Typed> _r = newDocumentReader(defs.jsonTypedProperties());
        JsonDocumentDefinitions.Typed _result = _r.read(newReader("{\"integer\":1,\"float\":2.0,\"boolean\":true}"));
        Assert.assertNotNull(_result);
        Assert.assertEquals(1, _result.getInteger());
        Assert.assertEquals(2.0f, _result.getFloat(), 0f);
        Assert.assertTrue(_result.getBoolean());
    }

    private Reader newReader(String aString) {
        return new StringReader(aString);
    }
}
