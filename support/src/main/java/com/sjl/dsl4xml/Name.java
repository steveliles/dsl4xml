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

}
