package com.sjl.dsl4xml.support;

import java.lang.reflect.*;
import java.util.*;

import com.sjl.dsl4xml.*;

public class Classes {

    public static final String[] MUTATOR_PREFIXES = {"set", "add", "insert", "put"};
    
	public static <T> Method getMutatorMethod(Class<T> aClass, String... aMaybeNames) {
	    Set<String> _names = new LinkedHashSet<String>();
	    for (String _s : aMaybeNames) {
	        String _suffix = upperCaseFirstLetter(_s);	        
	        for (String _prefix : MUTATOR_PREFIXES) {
	            _names.add(_prefix + _suffix);
	        }	        
	    }
	    
	    for (String _s : MUTATOR_PREFIXES) {
	    	_names.add(_s);
	    }
	    
	    return getMethod(aClass, _names);
	}
	
	private static String upperCaseFirstLetter(String aString) {
		return ("" + aString.charAt(0)).toUpperCase() + aString.substring(1, aString.length());
	}
	
	private static <T> Method getMethod(Class<T> aClass, Collection<String> aNames) {
	    for (String _name : aNames) {
			for (Method _m : aClass.getMethods()) {
				if (_name.equals(_m.getName()) && (_m.getParameterTypes().length == 1)) {					
					_m.setAccessible(true); // allow to invoke non-public methods
					return _m;
				}
			}
		}
		
	    String _classname = 
            (aClass.isAnonymousClass() || aClass.isSynthetic() || aClass.getName().matches(".*\\$Proxy.*")) ? 
                asString(aClass, aClass.getInterfaces()) : aClass.getName();
           
       String _msg = "No mutator in class " + _classname + ", tried ";
       for (String _name : aNames) {
           _msg += _name + ",";
       }
       throw new NoSuitableMethodException(_msg);
	}
	
	public static <T> T newInstance(Class<T> aClass) {
	    try {
            if (aClass.isInterface()) {
                return (T) newDynamicProxy(aClass);
            } else {        
                return (T) aClass.newInstance();
            }
        } catch (Exception anExc) {
            throw new XmlReadingException(anExc);
        }
	}
	
	public static <T> T newDynamicProxy(Class<T> aClass) {
		if (Iterable.class.isAssignableFrom(aClass)) {
			return newListBasedProxy(aClass);
		} else {		
			return newMapBasedProxy(aClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newListBasedProxy(final Class<T> aClass) {
		return (T) Proxy.newProxyInstance(
			aClass.getClassLoader(),
			new Class<?>[]{ aClass },
			new ListBasedInvocationHandler<T>(aClass));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newMapBasedProxy(final Class<T> aClass) {
		return (T) Proxy.newProxyInstance(
			aClass.getClassLoader(),
			new Class<?>[]{ aClass },
			new MapBasedInvocationHandler(aClass)
		);
	}
	
	private static String asString(Class<?> aClass, Class<?>... aClasses) {
		return aClass.getName() + Arrays.asList(aClasses).toString();
	}
	
	private static class MapBasedInvocationHandler implements InvocationHandler {
		
		private Map<String, Object> map = new HashMap<String, Object>();
		private Class<?> clz;
		
		public MapBasedInvocationHandler(Class<?> aClass) {
			clz = aClass;
		}
		
		@Override
		public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {
			if ("toString".equals(aMethod.getName())) {
				return toString();
			} else if ("hashCode".equals(aMethod.getName())) {
			    return hashCode();
			} else if ("equals".equals(aMethod.getName())) {
				return equals(aArgs[0]);
			} else if (isMutator(aMethod)) {
				return map.put(getSuffix(aMethod.getName()), aArgs[0]);
			} else {
				return map.get(getSuffix(aMethod.getName()));
			}				    
		}
		
		private boolean isMutator(Method aMethod) {
			return (aMethod.getParameterTypes().length > 0);
		}
		
		private String getSuffix(String aMethodName) {
			return aMethodName.substring(3); // TODO
		}
		
		public String toString() {
			return "proxy(" + clz.getName() + ")" + map;
		}
	}
	
	private static class ListBasedInvocationHandler<T> implements InvocationHandler {
		
		private Map<String, Object> map = new HashMap<String, Object>();
		private List<T> list = new ArrayList<T>();
		private Class<?> clz;
		
		public ListBasedInvocationHandler(Class<?> aClass) {
			clz = aClass;
		}
		
		@Override
		public Object invoke(Object aProxy, Method aMethod, Object[] aArgs) throws Throwable {
			try {
				if ("toString".equals(aMethod.getName())) {
					return toString();
				} else if ("hashCode".equals(aMethod.getName())) {
				    return hashCode();
				} else if ("equals".equals(aMethod.getName())) {
					return equals(aArgs[0]);
				} else {
					if (isMethodOf(List.class, aMethod)) {
						return aMethod.invoke(list, aArgs);
					} else {
						if (isMutator(aMethod)) {
							return map.put(getSuffix(aMethod.getName()), aArgs[0]);
						} else {
							return map.get(getSuffix(aMethod.getName()));
						}
					}
				}
			} catch (InvocationTargetException anExc) {
				throw anExc.getCause();
			} catch (IllegalAccessException anExc) {
				throw anExc;
			}
		}
			
		// todo: cache for efficiency?
		private boolean isMethodOf(Class<?> aClass, Method aMethod) {
			for (Method _m : aClass.getMethods()) {
				if (_m.getName().equals(aMethod.getName())) {
					if (_m.getParameterTypes().length == aMethod.getParameterTypes().length) {
						return true;
					}
				}
			}
			return false;
		}
			
		private boolean isMutator(Method aMethod) {
			return (aMethod.getParameterTypes().length > 0);
		}
		
		private String getSuffix(String aMethodName) {
			return aMethodName.substring(3); // TODO
		}
		
		public String toString() {
			return "proxy(" + clz.getName() + ")" + list;
		}
	}
}
