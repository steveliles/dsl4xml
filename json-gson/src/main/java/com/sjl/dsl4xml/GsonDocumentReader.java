package com.sjl.dsl4xml;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sjl.dsl4xml.gson.*;

import java.io.IOException;
import java.io.Reader;

/**
 * @author steve
 */
public class GsonDocumentReader<T> extends AbstractDocumentReader<T>
{
	public static <R> GsonDocumentReader<R> mappingOf(Class<R> aClass) {
		return new GsonDocumentReader<R>(aClass);
	}

	public static <R> ObjectHandler<R> object(String aName) {
		return new ObjectHandler<R>(aName);
	}

	public static <R> ObjectHandler<R> object(String aName, Class<R> aContextType) {
		return new ObjectHandler<R>(aName, aContextType);
	}

	public static <R> ObjectHandler<R> object(Class<R> aContextType) {
		return new ObjectHandler<R>(aContextType);
	}

	public static <R> PropertyHandler<R> property(String aName) {
		return new PropertyHandler<R>(aName);
	}

	public static <R> ArrayHandler<R> array(String aName, Class<R> aClass) {
		return new ArrayHandler<R>(aName, aClass);
	}

	private JsonHandler[] handlers = new JsonHandler[]{};

	public GsonDocumentReader(Class<T> aClass) {
		super(aClass);
	}

	@Override
	public T read(Reader aReader)
	throws ParsingException {
		JsonReader _reader = null;
		try {
			_reader = new JsonReader(aReader);
			Context _ctx = new GsonContext(_reader);
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

	public GsonDocumentReader<T> to(JsonHandler... aHandlers) {
		handlers = aHandlers;
		return this;
	}
}
