package com.sjl.dsl4xml.json;

import java.io.Reader;

public class JsonDocumentMapper<T> {

    private DocumentDefinition<T> definition;

    public JsonDocumentMapper(DocumentDefinition<T> aDefinition) {
        definition = aDefinition;
    }

    public T map(Reader aReader){
        return null;
    }

}
