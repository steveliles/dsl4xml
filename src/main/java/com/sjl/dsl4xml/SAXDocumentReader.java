package com.sjl.dsl4xml;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import com.sjl.dsl4xml.support.*;
import com.sjl.dsl4xml.support.convert.*;

public class SAXDocumentReader<T> extends AbstractDocumentReader<T> {

	public static <R> SAXDocumentReader<R> mappingOf(String aTagName, Class<R> aClass) {
		return new SAXDocumentReader<R>(
			aClass, new TagHandler<R>(aTagName, aClass)
		);
	}
	
	public static <R> TagHandler<R> tag(String aTagName) {
		return new TagHandler<R>(aTagName);
	}
	
	public static <R> TagHandler<R> tag(String aTagName, Class<R> aClass) {
		return new TagHandler<R>(aTagName, aClass);
	}
	
	public static AttributesHandler attributes(String... anAttributeNames) {
		return new AttributesHandler(anAttributeNames);
	}
	
	private XMLReader reader;
	private TagHandler<T> root;
	private Dsl4XmlContentHandler<T> handler;
	
	public SAXDocumentReader(Class<T> aResultType, TagHandler<T> aRootTagHandler) {
		super(aResultType);
		try {
			SAXParserFactory _f = SAXParserFactory.newInstance();
		    SAXParser _p = _f.newSAXParser();
		    
		    root = aRootTagHandler;
		    reader = _p.getXMLReader();
		    handler = new Dsl4XmlContentHandler<T>(new DocHandler<T>(aRootTagHandler));
		    reader.setContentHandler(handler);
		} catch (Exception anExc) {
			throw new XmlReadingException(anExc);
		}
	}
	
	public SAXDocumentReader<T> to(TagHandler<?>... aHandlers) {
		root.to(aHandlers);
		return this;
	}
	
	@Override
	public T read(Reader aReader) throws XmlReadingException {
		handler.prepare(converters);
		try {
			reader.parse(new InputSource(aReader));
			return handler.getResult();
		} catch (XmlReadingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
anExc.printStackTrace();			
			throw new XmlReadingException(anExc);
		}
	}
	
	static class Dsl4XmlContentHandler<R> extends DefaultHandler {
		
		private Context context;
		private Handler<?> root;
		private Handler<?> handler;
		
		public Dsl4XmlContentHandler(DocHandler<R> aRootTagHandler) {
			root = aRootTagHandler;
			handler = aRootTagHandler;
		}
		
		public void prepare(Converter<?>... aConverters) {
			context = new Context(aConverters);
			handler = root;
		}
		
		@SuppressWarnings("unchecked")
		public R getResult() {
			return (R) context.getResult();
		}

		@Override
		public void startElement(
			String aUri, String aLocalName, 
			String aQName, Attributes anAttributes) 
		throws SAXException {
			handler = handler.startTag(aQName, anAttributes, context);
		}
		
		@Override
		public void endElement(
			String aUri, String aLocalName, String aQName
		) throws SAXException {
			handler = handler.moveUp(aQName, context);
		}

		@Override
		public void characters(char[] aChars, int aStart, int aLength)
		throws SAXException {
			handler = handler.characters(aChars, aStart, aLength, context);
		}
	}
	
	static class Context implements HasConverters {
		private Stack<Object> ctx;
		private Object result;
		private List<Converter<?>> converters;
		
		public Context(Converter<?>... aConverters) {
			converters = new ArrayList<Converter<?>>();
			
			registerConverters(
				new PrimitiveBooleanConverter(),
				new PrimitiveByteConverter(),
				new PrimitiveShortConverter(),
				new PrimitiveIntConverter(),
				new PrimitiveLongConverter(),
				new PrimitiveCharConverter(),
				new PrimitiveFloatConverter(),
				new PrimitiveDoubleConverter(),
				new BooleanConverter(),
				new ByteConverter(),
				new ShortConverter(),
				new IntegerConverter(),
				new LongConverter(),
				new CharacterConverter(),
				new FloatConverter(),
				new DoubleConverter(),
				new ClassConverter(),
				new StringConverter()
			);
			
			if (aConverters != null)
				converters.addAll(0, Arrays.asList(aConverters));
		}
		
		public void push(Object anObject) {
			if (ctx == null) {
				ctx = new Stack<Object>();
				result = anObject;
			}
			ctx.push(anObject);			
		}
		
		public Object peek() {
			return (ctx == null) ? null : ctx.peek();
		}
		
		public Object pop() {	
			return (ctx == null) ? null : ctx.pop();
		}
		
		public Object getResult() {
			return result;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> Converter<T> getConverter(Class<T> aArgType) {
			for (Converter<?> _c : converters) {
				if (_c.canConvertTo(aArgType)) {
					return (Converter<T>) _c;
				}
			}
			throw new RuntimeException("No converter registered that can convert to " + aArgType);
		}

		@Override
		public void registerConverters(Converter<?>... aConverters) {
			// push any registered converters on ahead of existing converters (allows simple override)
			converters.addAll(0, Arrays.asList(aConverters));
		}
	}
	
	static class InvalidStateException extends RuntimeException {
		public InvalidStateException(String aMessage) {
			super(aMessage);
		}
	}
	
	public interface Handler<R> {
		
		public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx);
		
		public Handler<?> moveUp(String aQName, Context aCtx);
		
		public Handler<?> characters(char[] aChars, int aStart, int aLength, Context aContext);
		
	}
	
	public static class DocHandler<R> implements Handler<R> {

		private TagHandler<R> root;
		
		public DocHandler(TagHandler<R> aRoot) {
			root = aRoot;
		}
		
		@Override
		public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx) {			
			if (root.handlesTag(aQName)) {
				return root.moveDown(aQName, anAttributes, aCtx);
			} else {
				throw new InvalidStateException("unexpected root element: " + aQName);
			}
		}

		@Override
		public Handler<?> moveUp(String aQName, Context aCtx) {
			return root;
		}

		@Override
		public Handler<?> characters(char[] aChars, int aStart, int aLength, Context aContext) {
			return root.characters(aChars, aStart, aLength, aContext);
		}
	}
		
	public static class TagHandler<R> implements Handler<R> {
		
		private String tagName;
		private Class<R> modelType;
		
		private TagHandler<?> parent;
		private TagHandler<?>[] tags;
		private AttributesHandler attributes;
		private TextHandler text;
		private IgnoreHandler<R> ignore;
		private ContextMutator<R> mutator;
		
		public TagHandler(String aTagName, Class<R> aModelType) {
			tagName = aTagName;
			modelType = aModelType;
			parent = this;
		}
		
		public TagHandler(String aTagName) {
			tagName = aTagName;
		}
		
		public TagHandler<R> with(AttributesHandler anAttributes) {
			attributes = anAttributes;
			return this;
		}
		
		public TagHandler<R> with(TagHandler<?>... aTags) {
			return to(aTags);
		}
		
		public TagHandler<R> withPCDataMappedTo(String aFieldName) {
			text = new TextHandler(aFieldName);
			return this;
		}
		
		public TagHandler<R> to(TagHandler<?>... aTags) {			
			tags = aTags;
			for (int i=0; i<aTags.length; i++) {
				tags[i].setParent(this);
			}
			return this;
		}
		
		public void setParent(TagHandler<?> aParent) {
			if ((parent != null) && (parent != this))
				throw new InvalidStateException("Parent is already set!");
			parent = aParent;			
		}
		
		public boolean handlesTag(String aName) {
			return tagName.equals(aName);
		}
		
		public TagHandler<?> moveDown(String aQName, Attributes anAttributes, Context aCtx) {
			try {
				if (modelType != null) {
					Object _parent = aCtx.peek();
					R _model = newContextObject();
					aCtx.push(_model);
					if (_parent != null) {
						ContextMutator<R> _m = getMutator(_parent.getClass(), modelType);
						_m.apply(_parent, _model);
					}
				}
				
				if (attributes != null)
					attributes.handle(aCtx, aCtx.peek(), anAttributes);
				
				return this;
			} catch (XmlReadingException anExc) {
				throw anExc;
			} catch (Exception anExc) {
				throw new XmlReadingException(anExc);
			}
		}

		public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx) {
			if (text != null) {
				text.complete(aCtx);
			}
			
			if (tags == null) {
				return this;
			}
			
			for (TagHandler<?> _h : tags) {
				if (_h.handlesTag(aQName)) {
					return _h.moveDown(aQName, anAttributes, aCtx);
				}
			}
			return getIgnore();
		}

		public TagHandler<?> moveUp(String aQName, Context aCtx) {
			if (text != null) {
				text.complete(aCtx);
			}
			if (modelType != null) {
				aCtx.pop();
			}			
			return parent;
		}

		public TagHandler<R> characters(char[] aChars, int aStart, int aLength, Context aContext) {
			if (tags == null && text == null) {
				text = new TextHandler(tagName);
			}
			
			if (text != null) {				
				text.handle(aChars, aStart, aLength, aContext);
			}
			return this;
		}
		
		private Handler<R> getIgnore() {
			if (ignore == null) {
				ignore = new IgnoreHandler<R>(this);
				return ignore;
			} else {
				return ignore;
			}
		}
		
		private R newContextObject() {
			try {
				return (R) modelType.newInstance();
			} catch (Exception anExc) {
				throw new XmlReadingException(anExc);
			}
		}
		
		private ContextMutator<R> getMutator(Class<?> aFor, Class<R> aWith) {
			if (mutator == null) {
				mutator = new ContextMutator<R>(aFor, aWith);
			}
			return mutator;
		}

		private static class ContextMutator<R> {
			private Method method;
			
			public ContextMutator(Class<?> aFor, Class<R> aWith) {
				method = Classes.getMutatorMethod(aFor, aWith.getSimpleName());
			}
			
			public void apply(Object aTo, R aWith) {
				try {
					method.invoke(aTo, aWith);
				} catch (Exception anExc) {
					throw new XmlReadingException(anExc);
				}
			}
		}
		
		public String toString() {
			return tagName;
		}
	}
	
	static class IgnoreHandler<T> implements Handler<T> {
		private Handler<T> parent;
		private IgnoreHandler<T> child;
		
		public IgnoreHandler(Handler<T> aParent) {
			parent = aParent;
		}

		@Override
		public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx) {
			if (child == null) {
				child = new IgnoreHandler<T>(this);
			}
			return child;
		}

		@Override
		public Handler<?> moveUp(String aQName, Context aCtx) {
			return parent;
		}

		@Override
		public Handler<?> characters(char[] aChars, int aStart, int aLength, Context aContext) {
			return this;
		}
	}
	
	static class AttributesHandler {
		private String[] attributeNames;
		private ValueSetter[] mutators;
		
		public AttributesHandler(String... anAttributeNames) {
			attributeNames = anAttributeNames;
			mutators = new ValueSetter[anAttributeNames.length];
		}
		
		public boolean handle(HasConverters aHasConverters, Object aCtx, Attributes anAttributes) {
			if (aCtx != null) {
				for (int i=0; i<attributeNames.length; i++) {
					ValueSetter _vs = getValueSetter(i, aHasConverters, aCtx.getClass());
					_vs.invoke(aCtx, anAttributes.getValue(attributeNames[i]));
				}
			}
			return false;
		}
		
		private ValueSetter getValueSetter(int anIndex, HasConverters aHasConverters, Class<?> aCurrentContextClass) {
			if (mutators[anIndex] == null) {
				mutators[anIndex] = new ValueSetter(aHasConverters, aCurrentContextClass, attributeNames[anIndex]);
			}
			return mutators[anIndex];
		}
	}
	
	static class TextHandler {
		
		private ValueSetter setter;
		private String field;
		private StringBuilder chars;
		
		public TextHandler(String aFieldName) {
			field = aFieldName;
			chars = new StringBuilder();
		}
		
		public void handle(char[] aChars, int aStart, int aLength, Context aContext) {
			chars.append(aChars, aStart, aLength);
		}
		
		public void complete(Context aContext) {
			Object _currentContext = aContext.peek();	
			try {
				ValueSetter _vs = getSetter(aContext, _currentContext.getClass(), field);
				_vs.invoke(_currentContext, chars.toString());
				chars.setLength(0);
			} catch (XmlReadingException anExc) {
				throw anExc;
			} catch (Exception anExc) {
				throw new XmlReadingException(anExc);
			}
		}
		
		private ValueSetter getSetter(Context aContext, Class<?> aClass, String aFieldName) {
			if (setter == null) {
				setter = new ValueSetter(aContext, aClass, aFieldName);
			}
			return setter;
		}	
	}
}
