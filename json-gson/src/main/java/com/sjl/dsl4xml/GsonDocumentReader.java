package com.sjl.dsl4xml;

import com.google.gson.stream.JsonReader;
import com.sjl.dsl4xml.gson.Context;
import com.sjl.dsl4xml.gson.GsonContext;
import com.sjl.dsl4xml.gson.JsonHandler;
import com.sjl.dsl4xml.gson.ObjectHandler;

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

	private JsonHandler[] handlers = new JsonHandler[]{};

	public GsonDocumentReader(Class<T> aClass) {
		super(aClass);
	}

	@Override
	public T read(Reader aReader)
	throws ParsingException
	{
		JsonReader _reader = null;
		try
		{
			_reader = new JsonReader(aReader);
			Context _ctx = new GsonContext(_reader);
			if (converters != null)
				_ctx.registerConverters(converters);
			_ctx.push(newResultObject());

			try
			{
				while (_ctx.hasNext())
				{
					for (JsonHandler _h : handlers)
					{
						if (_h.read(_ctx))
						{
							break;
						}
					}

					if (_ctx.hasNext())
						_ctx.next();
				}
				return _ctx.peek();
			}
			catch (ParsingException anExc)
			{
				throw anExc;
			}
			catch (Exception anExc)
			{
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

	public GsonDocumentReader<T> to(ObjectHandler... aHandlers) {
		handlers = aHandlers;
		return this;
	}
}
