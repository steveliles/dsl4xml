package com.sjl.dsl4xml.sax;

import java.lang.reflect.*;

import org.xml.sax.*;

import com.sjl.dsl4xml.*;
import com.sjl.dsl4xml.support.*;

public class TagHandler<R> implements Handler<R> {
	
	private String tagName;
	private Class<R> modelType;
	private Factory<R,?> factory;
	
	private TagHandler<?> parent;
	private TagHandler<?>[] tags;
	private AttributesHandler attributes;
	private TextHandler text;
	private IgnoreHandler<R> ignore;
	private ContextMutator mutator;
	
	public TagHandler(String aTagName, Class<R> aModelType) {
		tagName = aTagName;
		modelType = aModelType;
		parent = this;
	}

	public TagHandler(String aTagName, Factory<R,?> aFactory) {
		tagName = aTagName;
		factory = aFactory;
		parent = this;
	}

	public TagHandler(String aTagName) {
		tagName = aTagName;
		parent = this;
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
				R _model = Classes.newInstance(modelType);
				aCtx.push(_model);
			} else if (factory != null) {
				R _model = factory.newIntermediary();
				aCtx.push(_model);
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
			R _model = (R) aCtx.pop();

			if (parent != null) {
				Object _parent = aCtx.peek();
				if (_parent != null) {
					ContextMutator _m = getMutator(_parent.getClass(), modelType, tagName);
					_m.apply(_parent, _model);
				}
			}
		} else if (factory != null) {
			R _model = (R) aCtx.pop();

			if (parent != null) {
				Object _parent = aCtx.peek();
				if (_parent != null) {
					Object _result = factory.newTarget(_model);
					ContextMutator _m = getMutator(_parent.getClass(), _result.getClass(), tagName);
					_m.apply(_parent, _result);
				}
			}
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

    private ContextMutator getMutator(Class<?> aFor, Class<?> aWith, String aTagName) {
        if (mutator == null) {
            mutator = new ContextMutator(aFor, aWith, aTagName);
        }
        return mutator;
    }

	public String toString() {
		return tagName;
	}

    private class ContextMutator {
        private Method method;
        private String tag;
        private boolean twoArgMutator;

        public ContextMutator(Class<?> aFor, Class<?> aWith, String aTagName) {
            method = Classes.getMutatorMethod(aFor, aWith.getSimpleName(), aTagName);
            twoArgMutator = (method.getParameterTypes().length == 2);
            tag = aTagName;
        }

        public void apply(Object aTo, Object aWith) {
            try {
                if (twoArgMutator) {
                    method.invoke(aTo, tag, aWith);
                } else {
                    method.invoke(aTo, aWith);
                }
            } catch (Exception anExc) {
                throw new ParsingException(
                    "invoking " + method.getName() +
                    " on " + aTo.getClass().getName() +
                    " with " + aWith.getClass().getName(), anExc);
            }
        }
    }
}