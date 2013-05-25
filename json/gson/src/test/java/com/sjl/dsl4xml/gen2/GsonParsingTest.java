package com.sjl.dsl4xml.gen2;

import com.sjl.dsl4xml.json.AbstractJsonParsingTest;
import com.sjl.dsl4xml.json.DocumentDefinition;
import com.sjl.dsl4xml.support.DocumentReader2;

public class GsonParsingTest extends AbstractJsonParsingTest {

    @Override
    protected <T> DocumentReader2<T> newDocumentReader(DocumentDefinition<T> aDefinition) {
        return new GsonDocumentReader<T>(aDefinition);
    }

}
