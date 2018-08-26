package com.xjd.gets;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author elvis.xu
 * @since 2018-08-26 13:13
 */
public abstract class Gets {
	private static final Logger log = LoggerFactory.getLogger(Gets.class);

	public static <T> T wrap(T source) {
		if (source instanceof Wrap) return source;
		if (source == null) {
			throw new IllegalArgumentException("The input parameter cannot be null or use wrap(T source, Class<T> clazz) instead");
		}
		return wrap(source, (Class<T>) source.getClass());
	}

	public static <T> T wrap(T source, Class<T> clazz) {
		if (source instanceof Wrap) return source;
		if (clazz == null) {
			throw new IllegalArgumentException("The class parameter cannot be null");
		}
		if (Modifier.isFinal(clazz.getModifiers())) {
			log.info("cannot proxy final class {}", clazz);
			return source;
		}

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		if (clazz.isInterface()) {
			enhancer.setInterfaces(new Class[]{Wrap.class, clazz});
		} else {
			enhancer.setInterfaces(new Class[]{Wrap.class});
		}
		enhancer.setCallback(new GetInterceptor<T>(source, clazz));
		T wrap = createWrap(enhancer, clazz, source);

		return wrap;
	}

	protected static <T> T createWrap(Enhancer enhancer, Class<T> clazz, T source) {
		try {
			return (T) enhancer.create();
		} catch (Exception e) {
			for (Constructor<?> constructor : clazz.getConstructors()) {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				Object[] args = new Object[parameterTypes.length];
				for (int i = 0; i < parameterTypes.length; i++) {
					args[i] = casualValueForType(parameterTypes[i]);
				}
				try {
					return (T) enhancer.create(parameterTypes, args);
				} catch (Exception t) {
					log.debug("try create instance of class '{}' using constructor({}) failed.", clazz, Arrays.toString(args), t);
				}
			}
		}
		log.info("cannot proxy class '{}' due to no constructor can be used to initiate it", clazz);
		return source;
	}

	protected static Object casualValueForType(Class clazz) {
		if (!clazz.isPrimitive()) {
			return null;
		}
		if (char.class.equals(clazz)) {
			return ' ';
		}
		if (boolean.class.equals(clazz)) {
			return false;
		}
		// other primitives are numbers
		return Byte.valueOf((byte) 0);
	}

	public static boolean isNull(Object object) {
		if (object instanceof Wrap) return ((Wrap) object)._source() == null;
		return object == null;
	}

	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}

	public static <T> T get(T object) {
		return get(object, null);
	}

	public static <T> T get(T object, T defaultValue) {
		T source = (object instanceof Wrap) ? ((Wrap<T>) object)._source() : object;
		return source != null ? source : defaultValue;
	}

	protected static interface Wrap<T> {
		T _source();
		T _class();
	}

	protected static class GetInterceptor<T> implements MethodInterceptor {
		protected T source;
		protected Class<T> clazz;

		public GetInterceptor(T source, Class<T> clazz) {
			this.source = source;
			this.clazz = clazz;
		}

		@Override
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			String methodName = method.getName();
			// Wrap
			if (methodName.equals("_source")) {
				return source;
			}
			if (methodName.equals("_class")) {
				return clazz;
			}
			// Object
			if (methodName.equals("getClass")) {
				return clazz;
			}
			if (methodName.equals("equals")) {
				return source == null ? source == args[0] : source.equals(args[0]);
			}
			Class<?> returnType = method.getReturnType();
			// return primitives
			if (returnType.isPrimitive()) {
				return proxy.invoke(source, args);
			}
			// return final type
			if (Modifier.isFinal(returnType.getModifiers())) {
				return source == null ? null : proxy.invoke(source, args);
			}
			// return non final type
			Object rt = source == null ? null : proxy.invoke(source, args);
			Object wrapRt = Gets.wrap(rt, (Class<Object>)(rt == null ? returnType : rt.getClass()));
			return wrapRt;
		}

	}

}
