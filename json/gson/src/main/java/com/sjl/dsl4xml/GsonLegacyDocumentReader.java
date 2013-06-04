package com.sjl.dsl4xml;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.gson.*;
import com.sjl.dsl4xml.support.Factory;

import java.io.*;
import java.util.List;

/**
 * @author steve
 */
public class GsonLegacyDocumentReader<T> extends AbstractLegacyDocumentReader<T>
{
	public static <R> GsonLegacyDocumentReader<R> mappingOf(Class<R> aClass) {
		return new GsonLegacyDocumentReader<R>(aClass);
	}

	public static <R> ObjectHandler<R> object(String aName, Class<R> aContextType) {
		return new ObjectHandler<R>(aName, aContextType);
	}

	public static <R> ObjectHandler<R> object(Class<R> aContextType) {
		return new ObjectHandler<R>(aContextType);
	}

	public static <I,R> PostConstructingObjectHandler<I,R> object(String aName, Class<I> aContextType, Factory<I,R> aFactory) {
		return new PostConstructingObjectHandler<I,R>(aName, aContextType, aFactory);
	}

	public static <I,R> PostConstructingObjectHandler<I,R> object(Class<I> aContextType, Factory<I,R> aFactory) {
		return new PostConstructingObjectHandler<I,R>(aContextType, aFactory);
	}

	public static <R> PropertyHandler<R> property(String aName) {
		return new PropertyHandler<R>(aName);
	}

	public static <R> PreConstructingArrayHandler<R> array(String aName, Class<R> aClass) {
		return new PreConstructingArrayHandler<R>(aName, aClass);
	}

	public static <I,R> PostConstructingArrayHandler<I,R> array(String aName, Factory<List<I>,R> aFactory) {
		return new PostConstructingArrayHandler<I,R>(aName, aFactory);
	}

	public static <R> UnNamedPropertyHandler<R> unNamedProperty(Class<R> aType) {
		return new UnNamedPropertyHandler<R>(aType);
	}

	private JsonHandler[] handlers = new JsonHandler[]{};

	public GsonLegacyDocumentReader(Class<T> aClass) {
		super(aClass);
	}

	@Override
	public T read(Reader aReader)
	throws ParsingException {
		JsonReader _reader = null;
		try {
			_reader = new JsonReader(aReader);
			GsonContext _ctx = new GsonContext(_reader);
			if (converters != null)
				_ctx.registerConverters(converters);
			_ctx.push(newResultObject());

			try {
				while (_ctx.isNotEndObject()) {
					for (JsonHandler _h : handlers) {
						if (_h.read(_ctx)) {
							break;
						}
					}

					if (_ctx.peek() == JsonToken.END_OBJECT) {
						_ctx.next();
						break;
					} else {
						_ctx.next();
					}

					if (!_ctx.hasNext())
						break;
				}
				return _ctx.peek();
			} catch (ParsingException anExc) {
				throw anExc;
			} catch (Exception anExc) {
				throw new ParsingException(anExc);
			}
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

	public GsonLegacyDocumentReader<T> to(JsonHandler... aHandlers) {
		handlers = aHandlers;
		return this;
	}
}
