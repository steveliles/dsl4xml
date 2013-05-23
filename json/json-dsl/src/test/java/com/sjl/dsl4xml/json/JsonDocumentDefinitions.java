package com.sjl.dsl4xml.json;

import com.sjl.dsl4xml.Converter;
import org.junit.Test;

import java.util.List;

public class JsonDocumentDefinitions {

    /**
     * {}
     */
    public void emptyDocument() {
        DocumentDefinition<Object> _d = new JsonDocumentDefinition<Object>(){{
            mapping(Object.class);
        }};
    }

    /**
     * { "myProperty":"some value" }
     */
    public interface Simple {
        public String getMyProperty();
    }

    public void simpleProperty() {
        DocumentDefinition<Simple> _d = new JsonDocumentDefinition<Simple>(){{
            mapping(Simple.class).with(
                property("myProperty")
            );
        }};
    }

    /**
     * {
     *   "integer":1,
     *   "float":1.0,
     *   "boolean":true
     * }
     */
    public interface Typed {
        public int getInteger();
        public float getFloat();
        public boolean getBoolean();
    }

    public void jsonTypedProperties() {
        DocumentDefinition<Typed> _d = new JsonDocumentDefinition<Typed>(){{
            mapping(Typed.class).with(
                number("integer", Integer.class),
                number("float", Float.class),
                bool("boolean")
            );
        }};
    }

    /**
     * {
     *   "first": {
     *     "myProperty":"first value"
     *   },
     *   "second": {
     *     "myProperty":"second value"
     *   }
     * }
     */
    public interface NestedObjects {
        public Simple getFirst();
        public Simple getSecond();
    }

    public void nestedObjects() {
        DocumentDefinition<NestedObjects> _either = new JsonDocumentDefinition<NestedObjects>() {{
            mapping(NestedObjects.class).with(
                object("first", Simple.class).with(
                    property("myProperty")
                ),
                object("second", Simple.class).with(
                    property("myProperty")
                )
            );
        }};

        // or we can define a method that defines the mapping of Simple to reduce repetition

        DocumentDefinition<NestedObjects> _or = new JsonDocumentDefinition<NestedObjects>() {{
                mapping(NestedObjects.class).with(
                    simple("first"),
                    simple("second")
                );
            }

            private NamedObject<Simple> simple(String aName) {
                return object(aName, Simple.class).with(
                    property("myProperty")
                );
            }
        };
    }

    /**
     * {
     *   "first-property": {
     *     "myProperty":"first value"
     *   },
     *   "second-property": {
     *     "myProperty":"second value"
     *   }
     * }
     */
    public void aliasedMethodNames() {
        DocumentDefinition<NestedObjects> _either = new JsonDocumentDefinition<NestedObjects>() {{
            mapping(NestedObjects.class).with(
                object(alias("first-property", "first"), Simple.class).with(
                    property("myProperty")
                ),
                object(alias("second-property", "second"), Simple.class).with(
                    property("myProperty")
                )
            );
        }};
    }

    /**
     * {
     *   "firstProperty":"I am first",
     *   "firstObject": {
     *     "myProperty":"first value"
     *   },
     *   "secondObject": {
     *     "myProperty":"second value"
     *   },
     *   "secondProperty":"I am second",
     * }
     */
    public interface MixedTypes {
        String getFirstProperty();
        Simple getFirstObject();
        Simple getSecondObject();
        String getSecondProperty();
    }

    public void mixedTypeHandling() {
        DocumentDefinition<MixedTypes> _d = new JsonDocumentDefinition<MixedTypes>(){{
            mapping(MixedTypes.class).with(
                property("firstProperty"),
                object("firstObject", Simple.class).with(
                    property("myProperty")
                ),
                object("secondObject", Simple.class).with(
                    property("myProperty")
                ),
                property("secondProperty")
            );
        }};
    }

    /**
     * {
     *   "someProperty":"hello",
     *   "strings":[
     *     "apples",
     *     "oranges",
     *     "bananas"
     *   ]
     * }
     */
    public interface SimplePropertyArray {
        String getSomeProperty();
        List<String> getStrings();
    }

    public void simplePropertyArrays() {
        DocumentDefinition<SimplePropertyArray> _either = new JsonDocumentDefinition<SimplePropertyArray>(){{
            mapping(SimplePropertyArray.class).with(
                property("someProperty"),
                array("strings").of(
                    property(String.class)
                )
            );
        }};

        // or, taking advantage of the array-property shortcut

        DocumentDefinition<SimplePropertyArray> _or = new JsonDocumentDefinition<SimplePropertyArray>(){{
            mapping(SimplePropertyArray.class).with(
                property("someProperty"),
                array("strings").of(String.class)
            );
        }};
    }

    /**
     * {
     *   "someProperty":"hello",
     *   "simples":[
     *     { "myProperty":"first" },
     *     { "myProperty":"second" },
     *     { "myProperty":"third" }
     *   ]
     * }
     */
    public interface ObjectArrays {
        String getSomeProperty();
        List<Simple> getSimples();
    }

    public void objectArrays() {
        DocumentDefinition<ObjectArrays> _d = new JsonDocumentDefinition<ObjectArrays>() {{
            mapping(ObjectArrays.class).with(
                property("someProperty"),
                array("simples").of(
                    object(Simple.class).with(
                        property("myProperty")
                    )
                )
            );
        }};
    }

    /**
     * {
     *     "arrays":[
     *         [ "one", "two", "three" ],
     *         [ "apples", "oranges" ],
     *         [ "one banana", "two banana" ]
     *     ]
     * }
     */
    public interface ArrayOfArrays {
        interface InnerArray {
            public List<String> getStrings();
        }

        public List<InnerArray> getArrays();
    }

    public void arraysOfArrays() {
        DocumentDefinition<ArrayOfArrays> _d = new JsonDocumentDefinition<ArrayOfArrays>(){{
            mapping(ArrayOfArrays.class).with(
                array("arrays").of(
                    array().of(String.class)
                )
            );
        }};
    }

    /**
     * {
     *     "first":"apple",
     *     "second":"ball"
     * }
     */
    public interface Interface {
        public String getFirst();
        public String getSecond();
    }

    final class Immutable implements Interface {
        final String first;
        final String second;

        public Immutable(String aFirst, String aSecond) {
            first = aFirst;
            second = aSecond;
        }

        @Override
        public String getFirst() {
            return first;
        }

        @Override
        public String getSecond() {
            return second;
        }
    }

    public void immutableRootTypes() {
        DocumentDefinition<Interface> _either = new JsonDocumentDefinition<Interface>(){{
            mapping(Immutable.class).via(Interface.class).with(
                property("first"),
                property("second")
            );
        }};

        // or

        DocumentDefinition<Immutable> _or = new JsonDocumentDefinition<Immutable>(){{
            mapping(Immutable.class).via(Interface.class).with(
                property("first"),
                property("second")
            );
        }};
    }

    public interface Uncommon {
        public String getFirst();
        public String getSecond();
    }

    public void intermediateRootTypeWithNoCommonHierarchy() {
        // a converter that can convert from Uncommon to Immutable
        // this could be written inline in the document definition,
        // and, with Java8 lambdas, could be quite succinct.
        final Converter<Uncommon, Immutable> uncommonToImmutable = new Converter<Uncommon, Immutable>() {
            public Immutable convert(Uncommon aFrom) {
                return new Immutable(aFrom.getFirst(), aFrom.getSecond());
            }
        };

        DocumentDefinition<Immutable> _either = new JsonDocumentDefinition<Immutable>(){{
            converting(Uncommon.class).to(Immutable.class).using(uncommonToImmutable);

            mapping(Immutable.class).via(Uncommon.class).with(
                property("first"),
                property("second")
            );
        }};

        // or

        DocumentDefinition<Immutable> _or = new JsonDocumentDefinition<Immutable>(){{
            mapping(Immutable.class).
                via(Uncommon.class, uncommonToImmutable).with(
                    property("first"),
                    property("second")
            );
        }};
    }

    public interface Immutables {
        Immutable first();
        Immutable second();
    }

    @Test
    public void immutableNonRootTypes() {
        DocumentDefinition<Immutables> _d = new JsonDocumentDefinition<Immutables>(){{
            converting(Interface.class).to(Immutable.class).using(
                new Converter<Interface, Immutable>(){
                    public Immutable convert(Interface aFrom) {
                        return new Immutable(aFrom.getFirst(), aFrom.getSecond());
                    }
                }
            );
            converting(Uncommon.class).to(Immutable.class).using(
                new Converter<Uncommon, Immutable>(){
                    public Immutable convert(Uncommon aFrom) {
                        return new Immutable(aFrom.getFirst(), aFrom.getSecond());
                    }
                }
            );

            mapping(Immutables.class).with(
                // via a class that is interface compatible,
                object(Immutable.class).via(Interface.class).with(
                    property("first"),
                    property("second")
                ),
                // and again, this time via a class that is not interface compatible
                object(Immutable.class).via(Uncommon.class).with(
                    property("first"),
                    property("second")
                )
            );
        }};
    }
}
