package com.sjl.dsl4xml.gson;

import com.sjl.dsl4xml.gson.GsonDocumentReader;
import com.sjl.dsl4xml.json.AbstractJsonParsingTest;
import com.sjl.dsl4xml.json.DocumentDefinition;
import com.sjl.dsl4xml.DocumentReader;

public class GsonParsingTest extends AbstractJsonParsingTest {

    @Override
    protected <T> DocumentReader<T> newDocumentReader(DocumentDefinition<T> aDefinition) {
        return new GsonDocumentReader<T>(aDefinition);
    }

}
