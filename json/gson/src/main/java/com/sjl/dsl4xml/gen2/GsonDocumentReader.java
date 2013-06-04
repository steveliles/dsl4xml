package com.sjl.dsl4xml.gen2;

import com.google.gson.stream.JsonReader;
import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.json.DocumentDefinition;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.DocumentReader;

import java.io.*;

public class GsonDocumentReader<T> implements DocumentReader<T> {

    private Builder<T> builder;

    public GsonDocumentReader(DocumentDefinition<T> aDefinition) {
        builder = aDefinition.newBuilder();
    }

    public T read(InputStream anInputStream, String aCharset) {
        return read(newReader(anInputStream, aCharset));
    }

    public T read(Reader aReader)
    throws ParsingException {
        JsonReader _reader = null;
        try {
            _reader = new JsonReader(aReader);
            return new GsonContext(_reader).build(builder);
        } catch (ParsingException anExc) {
            throw anExc;
        } catch (Exception anExc) {
            throw new ParsingException(anExc);
        }
        finally {
            if (_reader != null) {
                try {
                    _reader.close();
                } catch (IOException anExc) {
                    throw new ParsingException("Exception while closing the reader", anExc);
                }
            }
        }
    }

    private Reader newReader(InputStream anInputStream, String aCharSet) {
        try {
            return new InputStreamReader(anInputStream, aCharSet);
        } catch (UnsupportedEncodingException anExc) {
            throw new RuntimeException(anExc);
        }
    }
}
