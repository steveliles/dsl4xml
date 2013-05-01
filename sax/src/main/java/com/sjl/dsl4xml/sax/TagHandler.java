package com.sjl.dsl4xml.sax;

import java.lang.reflect.*;

import org.xml.sax.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

public class TagHandler<R> implements Handler<R> {
	
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
	
	public TagHandler<R> with(AttributesHandler anAttributes, TagHandler<?>... aTags) {
		attributes = anAttributes;
		return to(aTags);
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
					ContextMutator<R> _m = getMutator(_parent.getClass(), modelType, tagName);
					_m.apply(_parent, _model);
				}
			}
			
			if (attributes != null)
				attributes.handle(aCtx, aCtx.peek(), anAttributes);
			
			return this;
		} catch (ParsingException anExc) {
			throw anExc;
		} catch (Exception anExc) {
			throw new ParsingException(anExc);
		}
	}

	public Handler<?> startTag(String aQName, Attributes anAttributes, Context aCtx) {
		if (text != null) {
			text.complete(aCtx);
		}
		
		if (tags == null) {
			return getIgnore();
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
		return Classes.newInstance(modelType);
	}
	
	private ContextMutator<R> getMutator(Class<?> aFor, Class<R> aWith, String aTagName) {
		if (mutator == null) {
			mutator = new ContextMutator<R>(aFor, aWith, aTagName);
		}
		return mutator;
	}

	private static class ContextMutator<R> {
		private Method method;
		
		public ContextMutator(Class<?> aFor, Class<R> aWith, String aTagName) {
			method = Classes.getMutatorMethod(aFor, aWith.getSimpleName(), aTagName);
		}
		
		public void apply(Object aTo, R aWith) {
			try {
				method.invoke(aTo, aWith);
			} catch (Exception anExc) {
				throw new ParsingException(
					"invoking " + method.getName() + 
					" on " + aTo.getClass().getName() + 
					" with " + aWith.getClass().getName(), anExc);
			}
		}
	}
	
	public String toString() {
		return tagName;
	}
}