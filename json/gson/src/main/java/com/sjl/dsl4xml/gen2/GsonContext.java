package com.sjl.dsl4xml.gen2;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.ParsingException;
import com.sjl.dsl4xml.support.Builder;
import com.sjl.dsl4xml.support.Context;
import com.sjl.dsl4xml.support.Name;

import java.util.List;
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

            JsonToken _token = null;
            while ((_token = reader.peek()) != null) {
System.out.println(_token);
                switch(_token) {
                    case NAME:
                        names.push(reader.nextName());
                        break;
                    case STRING:
                        Builder<?> _p = _b.moveDown(this);
                        if (_p == null)
                            _p = builders.peek();
System.out.println(_p);
                        _p.prepare(this);
                        _p.setValue(this, "", reader.nextString());
                        if (_b.isArray())
                            _b.setValue(this, "", _p.build(this));
                        else
                            _b.setValue(this, names.pop(), _p.build(this));
                        break;
                    case NUMBER:
                        _p = _b.moveDown(this);
                        if (_p == null)
                            _p = builders.peek();
                        _p.prepare(this);
                        _p.setValue(this, "", reader.nextDouble()); // TODO: this is not good - overflow on long's
                        if (_b.isArray())
                            _b.setValue(this, "", _p.build(this));
                        else
                            _b.setValue(this, names.pop(), _p.build(this));
                        break;
                    case BOOLEAN:
                        _p = _b.moveDown(this);
                        if (_p == null)
                            _p = builders.peek();
                        _p.prepare(this);
                        _p.setValue(this, "", reader.nextBoolean());
                        if (_b.isArray())
                            _b.setValue(this, "", _p.build(this));
                        else
                            _b.setValue(this, names.pop(), _p.build(this));
                        break;
                    case NULL:
                        reader.nextNull();
                        names.pop();
                    case BEGIN_ARRAY:
                        reader.beginArray();
                        _b = _b.moveDown(this);
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
                        if (_b.isArray())
                            _b.setValue(this, "", _p.build(this));
                        else
                            _b.setValue(this, names.pop(), _p.build(this));
                        break;
                    case BEGIN_OBJECT:
                        reader.beginObject();
                        _b = _b.moveDown(this);
                        if (_b == null) {
                            _b = builders.peek();
//                            if (reader.peek() == JsonToken.END_OBJECT)
//                                reader.endObject();
//                            else
//                                reader.skipValue();
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
                            if (_b.isArray())
                                _b.setValue(this, "", _p.build(this));
                            else
                                _b.setValue(this, names.pop(), _p.build(this));
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

    @Override
    public Builder<?> select(List<Builder<?>> aBuilders) {
        String _name = names.isEmpty() ? "" : names.peek();
        for (Builder<?> _b : aBuilders) {
            if ((_name.equals(_b.getName().getName())) ||
                (_b.getName().equals(Name.MISSING) && aBuilders.size()==1)
               ) {
                // TODO: use the type info too ? ...
                return _b;
            }
        }
        return null;
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
