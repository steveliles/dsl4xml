package com.sjl.dsl4xml;

public interface Name {

    public static final Name MISSING = new Name(){
        @Override
        public String getNamespace() {
            return "";
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getAlias() {
            return "";
        }
    };

    public String getNamespace();

    public String getName();

    public String getAlias();

    class Impl implements Name {

        private String name;
        private String alias;
        private String namespace;

        public Impl(String aName)
        {
            this(aName, aName);
        }

        public Impl(String aName, String anAlias)
        {
            name = aName;
            alias = anAlias;
        }

        @Override
        public String getNamespace() {
            return "";
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getAlias() {
            return (alias != null) ? alias : name;
        }

        @Override
        public String toString() {
            return "'" + name + "(" + alias + ")'";
        }

        @Override
        public int hashCode() {
            return getClass().hashCode() ^ name.hashCode() ^ alias.hashCode();
        }

        @Override
        public boolean equals(Object anObject) {
            if (anObject instanceof Name) {
                Name _other = (Name) anObject;
                return
                    ((name != null) && (name.equals(_other.getName()))) &&
                     ((alias != null) && (alias.equals(_other.getAlias())));

            }
            return false;
        }
    };

}
