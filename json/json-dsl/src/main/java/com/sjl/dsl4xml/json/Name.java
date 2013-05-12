package com.sjl.dsl4xml.json;

public interface Name {

    public static final Name MISSING = new Name(){
        @Override
        public String getName() {
            return "";
        }

        @Override
        public String getAlias() {
            return "";
        }
    };

    public String getName();

    public String getAlias();

}
