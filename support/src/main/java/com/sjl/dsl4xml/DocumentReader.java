package com.sjl.dsl4xml;

import java.io.InputStream;
import java.io.Reader;

public interface DocumentReader<T> {

    public T read(InputStream anInputStream, String aCharSet);

    public T read(Reader aReader);

}
