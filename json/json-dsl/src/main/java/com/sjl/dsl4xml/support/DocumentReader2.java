package com.sjl.dsl4xml.support;

import java.io.InputStream;
import java.io.Reader;

public interface DocumentReader2<T> {

    public T read(InputStream anInputStream, String aCharSet);

    public T read(Reader aReader);

}
