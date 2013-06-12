package com.sjl.dsl4xml.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.Context;
import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.support.Builder;

import java.util.Stack;

public class GsonContext implements Context {

    private JsonReader reader;
    private Stack<String> names;
    private Stack<Object> build;
    private Stack<Builder<?>> builders;

    public GsonContext(JsonReader aReader) {
        reader = aReader;

        names = new Stack<String>();
        build = new Stack<Object>();
        builders = new Stack<Builder<?>>();
    }

    public <T> T build(Builder<T> aBuilder) {
        try {
            Builder _b = aBuilder;
            builders.push(_b);
            _b.prepare(this);

            JsonToken _token;
            while ((_token = reader.peek()) != null) {
                switch(_token) {
                    case NAME:
                        names.push(reader.nextName());
                        break;
                    case STRING:
                        Builder<?> _p = _b.moveDown(getName());
                        if (_p == null)
                            _p = builders.peek();
                        _p.prepare(this);
                        _p.setValue(this, "", reader.nextString());
                        buildAndSet(_b, _p, "");
                        break;
                    case NUMBER:
                        _p = _b.moveDown(getName());
                        if (_p == null)
                            _p = builders.peek();
                        _p.prepare(this);
                        _p.setValue(this, names.peek(), reader.nextDouble());
                        buildAndSet(_b, _p, "number");
                        break;
                    case BOOLEAN:
                        _p = _b.moveDown(getName());
                        if (_p == null)
                            _p = builders.peek();
                        _p.prepare(this);
                        _p.setValue(this, "", reader.nextBoolean());

                        buildAndSet(_b, _p, "bool");
                        break;
                    case NULL:
                        reader.nextNull();
                        names.pop();
                        break;
                    case BEGIN_ARRAY:
                        reader.beginArray();
                        _b = _b.moveDown(getName());
                        if (_b == null) {
                            _b = builders.peek();
                            if (reader.peek() == JsonToken.END_ARRAY)
                                reader.endArray();
                            else
                                reader.skipValue();
                        } else {
                            _b.prepare(this);
                            builders.push(_b);
                        }
                        break;
                    case END_ARRAY:
                        reader.endArray();
                        _p = builders.pop();
                        _b = builders.peek();
                        buildAndSet(_b, _p, "");
                        break;
                    case BEGIN_OBJECT:
                        reader.beginObject();
                        _b = _b.moveDown(getName());
                        if (_b == null) {
                            _b = builders.peek();
                        } else {
                            _b.prepare(this);
                            builders.push(_b);
                        }
                        break;
                    case END_OBJECT:
                        reader.endObject();
                        _p = builders.pop();
                        if (!builders.isEmpty())
                        {
                            _b = builders.peek();
                            buildAndSet(_b, _p, "");
                        }
                        break;
                    case END_DOCUMENT:
                        return aBuilder.build(this);
                }
            }
            return aBuilder.build(this);
        } catch (ParsingException anExc) {
            throw anExc;
        } catch (Exception anExc) {
            throw new ParsingException(anExc);
        }
    }

    private String getName()
    {
        return names.isEmpty() ? "" : names.peek();
    }

    private void buildAndSet(Builder aParentBuilder, Builder<?> aCurrentBuilder, String aTypeName) {
        String _name = "";
        try
        {
            Object _o = aCurrentBuilder.build(this);

            if (!aParentBuilder.isArray())
                _name = names.pop();

            if (_o != null)
                aParentBuilder.setValue(this, _name, _o);
        }
        catch (ClassCastException anExc)
        {
            throw new ParsingException(
                "Problem converting " + aTypeName + " '" + _name + "' in '" +
                aParentBuilder.getName().getName() + "' ... does your document definition use 'property' where it should use '" +
                aTypeName + "'?", anExc);
        }
    }

    @Override
    public void push(Object anObject) {
        build.push(anObject);
    }

    @Override
    public <T> T pop() {
        return (T)build.pop();
    }

    @Override
    public <T> T peek() {
        return (T)build.peek();
    }
}
