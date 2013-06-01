package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.support.DocumentReader2;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

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

    @Test
    public void testNestedObjects1() {
        DocumentReader2<JsonDocumentDefinitions.NestedObjects> _r = newDocumentReader(defs.nestedObjects1());
        Object _value = _r.read(newReader(
            "{\"first\":{\"myProperty\":\"1st\"},\"second\":{\"myProperty\":\"2nd\"}}"));
        Assert.assertTrue(_value instanceof JsonDocumentDefinitions.NestedObjects);
        JsonDocumentDefinitions.NestedObjects _result = (JsonDocumentDefinitions.NestedObjects)_value;
        Assert.assertNotNull(_result.getFirst());
        Assert.assertNotNull(_result.getSecond());
        Assert.assertEquals("1st", _result.getFirst().getMyProperty());
        Assert.assertEquals("2nd", _result.getSecond().getMyProperty());
    }

    @Test
    public void testNestedObjects2() {
        DocumentReader2<JsonDocumentDefinitions.NestedObjects> _r = newDocumentReader(defs.nestedObjects2());
        JsonDocumentDefinitions.NestedObjects _result = _r.read(newReader(
                "{\"first\":{\"myProperty\":\"1st\"},\"second\":{\"myProperty\":\"2nd\"}}"));

        Assert.assertNotNull(_result.getFirst());
        Assert.assertNotNull(_result.getSecond());
        Assert.assertEquals("1st", _result.getFirst().getMyProperty());
        Assert.assertEquals("2nd", _result.getSecond().getMyProperty());
    }

    @Test
    public void testAliasedMethodNames() {
        DocumentReader2<JsonDocumentDefinitions.NestedObjects> _r = newDocumentReader(defs.aliasedMethodNames());
        JsonDocumentDefinitions.NestedObjects _result = _r.read(newReader(
                "{\"first-property\":{\"myProperty\":\"1st\"},\"second-property\":{\"myProperty\":\"2nd\"}}"));

        Assert.assertNotNull(_result.getFirst());
        Assert.assertNotNull(_result.getSecond());
        Assert.assertEquals("1st", _result.getFirst().getMyProperty());
        Assert.assertEquals("2nd", _result.getSecond().getMyProperty());
    }

    @Test
    public void testMixedTypeHandling() {
        DocumentReader2<JsonDocumentDefinitions.MixedTypes> _r = newDocumentReader(defs.mixedTypeHandling());
        JsonDocumentDefinitions.MixedTypes _result = _r.read(newReader(
            "{\"firstProperty\":\"I am first\"," +
            "  \"firstObject\": {" +
            "    \"myProperty\":\"first value\"" +
            "  }," +
            "  \"secondObject\": {" +
            "    \"myProperty\":\"second value\"" +
            "  }," +
            "  \"secondProperty\":\"I am second\"" +
            "}"));

        Assert.assertNotNull(_result);
        Assert.assertEquals("I am first", _result.getFirstProperty());
        Assert.assertNotNull(_result.getFirstObject());
        Assert.assertNotNull("first value", _result.getFirstObject().getMyProperty());
        Assert.assertNotNull(_result.getSecondObject());
        Assert.assertNotNull("second value", _result.getSecondObject().getMyProperty());
        Assert.assertEquals("I am second", _result.getSecondProperty());
    }

    @Test
    public void testSimplePropertyArrays() {
        DocumentReader2<JsonDocumentDefinitions.SimplePropertyArray> _r = newDocumentReader(defs.simplePropertyArrays());
        JsonDocumentDefinitions.SimplePropertyArray _result = _r.read(newReader(
            "{\"someProperty\":\"hello\",\"strings\":[\"apples\",\"oranges\",\"bananas\"]}"
        ));

        Assert.assertNotNull(_result);
        Assert.assertEquals("hello", _result.getSomeProperty());
        Assert.assertNotNull(_result.getStrings());

        List<String> _strings = _result.getStrings();
        Assert.assertEquals(3, _strings.size());
        Assert.assertEquals("apples", _strings.get(0));
        Assert.assertEquals("oranges", _strings.get(1));
        Assert.assertEquals("bananas", _strings.get(2));
    }

    @Test
    public void testSimplePropertyArraysWithShortcut() {
        DocumentReader2<JsonDocumentDefinitions.SimplePropertyArray> _r = newDocumentReader(defs.simplePropertyArraysWithShortcut());
        JsonDocumentDefinitions.SimplePropertyArray _result = _r.read(newReader(
                "{\"someProperty\":\"hello\",\"strings\":[\"apples\",\"oranges\",\"bananas\"]}"
        ));

        Assert.assertNotNull(_result);
        Assert.assertEquals("hello", _result.getSomeProperty());
        Assert.assertNotNull(_result.getStrings());

        List<String> _strings = _result.getStrings();
        Assert.assertEquals(3, _strings.size());
        Assert.assertEquals("apples", _strings.get(0));
        Assert.assertEquals("oranges", _strings.get(1));
        Assert.assertEquals("bananas", _strings.get(2));
    }

    private Reader newReader(String aString) {
        return new StringReader(aString);
    }
}
