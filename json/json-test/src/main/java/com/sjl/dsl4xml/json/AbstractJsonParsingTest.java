package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.DocumentReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public abstract class AbstractJsonParsingTest {

    protected abstract <T> DocumentReader<T> newDocumentReader(DocumentDefinition<T> aDefinition);

    private JsonDocumentDefinitions defs = new JsonDocumentDefinitions();

    @Test
    public void testEmptyDocument(){
        DocumentReader<Object> _r = newDocumentReader(defs.emptyDocument());
        Object _result = _r.read(newReader("{}"));
        Assert.assertNotNull(_result);
    }

    @Test
    public void testSimpleDocument() {
        DocumentReader<JsonDocumentDefinitions.Simple> _r = newDocumentReader(defs.simpleProperty());
        JsonDocumentDefinitions.Simple _result = _r.read(newReader("{\"myProperty\":\"some value\"}"));
        Assert.assertNotNull(_result);
        Assert.assertEquals("some value", _result.getMyProperty());
    }

    @Test
    public void testJsonTypedProperties() {
        DocumentReader<JsonDocumentDefinitions.Typed> _r = newDocumentReader(defs.jsonTypedProperties());
        JsonDocumentDefinitions.Typed _result = _r.read(newReader("{\"integer\":1,\"float\":2.0,\"boolean\":true}"));
        Assert.assertNotNull(_result);
        Assert.assertEquals(1, _result.getInteger());
        Assert.assertEquals(2.0f, _result.getFloat(), 0f);
        Assert.assertTrue(_result.getBoolean());
    }

    @Test
    public void testNestedObjects1() {
        DocumentReader<JsonDocumentDefinitions.NestedObjects> _r = newDocumentReader(defs.nestedObjects1());
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
        DocumentReader<JsonDocumentDefinitions.NestedObjects> _r = newDocumentReader(defs.nestedObjects2());
        JsonDocumentDefinitions.NestedObjects _result = _r.read(newReader(
                "{\"first\":{\"myProperty\":\"1st\"},\"second\":{\"myProperty\":\"2nd\"}}"));

        Assert.assertNotNull(_result.getFirst());
        Assert.assertNotNull(_result.getSecond());
        Assert.assertEquals("1st", _result.getFirst().getMyProperty());
        Assert.assertEquals("2nd", _result.getSecond().getMyProperty());
    }

    @Test
    public void testAliasedMethodNames() {
        DocumentReader<JsonDocumentDefinitions.NestedObjects> _r = newDocumentReader(defs.aliasedMethodNames());
        JsonDocumentDefinitions.NestedObjects _result = _r.read(newReader(
                "{\"first-property\":{\"myProperty\":\"1st\"},\"second-property\":{\"myProperty\":\"2nd\"}}"));

        Assert.assertNotNull(_result.getFirst());
        Assert.assertNotNull(_result.getSecond());
        Assert.assertEquals("1st", _result.getFirst().getMyProperty());
        Assert.assertEquals("2nd", _result.getSecond().getMyProperty());
    }

    @Test
    public void testMixedTypeHandling() {
        DocumentReader<JsonDocumentDefinitions.MixedTypes> _r = newDocumentReader(defs.mixedTypeHandling());
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
        DocumentReader<JsonDocumentDefinitions.SimplePropertyArray> _r = newDocumentReader(defs.simplePropertyArrays());
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
        DocumentReader<JsonDocumentDefinitions.SimplePropertyArray> _r = newDocumentReader(defs.simplePropertyArraysWithShortcut());
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
    public void testObjectArrays() {
        DocumentReader<JsonDocumentDefinitions.ObjectArrays> _r = newDocumentReader(defs.objectArrays());
        JsonDocumentDefinitions.ObjectArrays _result = _r.read(newReader(
            "{\"someProperty\":\"hello\",\"simples\":[{\"myProperty\":\"first\"},{\"myProperty\":\"second\"},{\"myProperty\":\"third\"}]}"
        ));

        Assert.assertNotNull(_result);
        Assert.assertEquals("hello", _result.getSomeProperty());
        Assert.assertNotNull(_result.getSimples());
        Assert.assertEquals(3, _result.getSimples().size());
        Assert.assertEquals("first", _result.getSimples().get(0).getMyProperty());
        Assert.assertEquals("second", _result.getSimples().get(1).getMyProperty());
        Assert.assertEquals("third", _result.getSimples().get(2).getMyProperty());
    }

    @Test
    public void testArrayOfArrays() {
        DocumentReader<JsonDocumentDefinitions.ArrayOfArrays> _r = newDocumentReader(defs.arraysOfArrays());
        JsonDocumentDefinitions.ArrayOfArrays _result = _r.read(newReader(
            "{\"arrays\":[[\"one\",\"two\",\"three\"],[\"apples\",\"oranges\"],[\"one banana\",\"two banana\"]]}"
        ));

        Assert.assertNotNull(_result);
        Assert.assertEquals(3, _result.getArrays().size());
        Assert.assertEquals(3, _result.getArrays().get(0).size());
        Assert.assertEquals("one", _result.getArrays().get(0).get(0));
        Assert.assertEquals("two", _result.getArrays().get(0).get(1));
        Assert.assertEquals("three", _result.getArrays().get(0).get(2));
        Assert.assertEquals(2, _result.getArrays().get(1).size());
        Assert.assertEquals("apples", _result.getArrays().get(1).get(0));
        Assert.assertEquals("oranges", _result.getArrays().get(1).get(1));
        Assert.assertEquals(2, _result.getArrays().get(2).size());
        Assert.assertEquals("one banana", _result.getArrays().get(2).get(0));
        Assert.assertEquals("two banana", _result.getArrays().get(2).get(1));
    }

    @Test
    public void testPopulatesImmutableRootTypesFromIntermediates() {
        DocumentReader<JsonDocumentDefinitions.Immutable> _r = newDocumentReader(defs.immutableRootTypes());
        JsonDocumentDefinitions.Immutable _result = _r.read(newReader(
            "{\"first\":\"apple\",\"second\":\"ball\"}"
        ));

        Assert.assertNotNull(_result);
        Assert.assertEquals("apple", _result.getFirst());
        Assert.assertEquals("ball", _result.getSecond());
    }

    @Test
    public void testPopulatesImmutableRootTypesFromIntermediatesWithNoCommonHierarchy() {
        DocumentReader<JsonDocumentDefinitions.Immutable> _r = newDocumentReader(defs.intermediateRootTypeWithNoCommonHierarchy());
        JsonDocumentDefinitions.Immutable _result = _r.read(newReader(
            "{\"first\":\"apple\",\"second\":\"ball\"}"
        ));

        Assert.assertNotNull(_result);
        Assert.assertEquals("apple", _result.getFirst());
        Assert.assertEquals("ball", _result.getSecond());
    }

    @Test
    public void testPopulatesImmutableNonRootTypes() {
        DocumentReader<JsonDocumentDefinitions.Immutables> _r = newDocumentReader(defs.immutableNonRootTypes());
        JsonDocumentDefinitions.Immutables _result = _r.read(newReader(
            "{\"first\":{\"first\":\"apple\",\"second\":\"ball\"},\"second\":{\"first\":\"apple\",\"second\":\"ball\"}}"
        ));

        Assert.assertNotNull(_result);
        Assert.assertNotNull(_result.first());
        Assert.assertNotNull(_result.second());
    }

    private Reader newReader(String aString) {
        return new StringReader(aString);
    }
}
