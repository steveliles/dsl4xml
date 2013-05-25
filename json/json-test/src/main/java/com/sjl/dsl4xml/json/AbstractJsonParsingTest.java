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

    private Reader newReader(String aString) {
        return new StringReader(aString);
    }
}
