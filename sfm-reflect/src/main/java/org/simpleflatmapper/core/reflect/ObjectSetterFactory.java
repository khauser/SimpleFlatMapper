package org.simpleflatmapper.core.reflect;

import org.simpleflatmapper.core.reflect.asm.AsmFactory;
import org.simpleflatmapper.core.reflect.getter.FieldSetter;
import org.simpleflatmapper.core.reflect.getter.MethodSetter;
import org.simpleflatmapper.core.reflect.primitive.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 */
public final class ObjectSetterFactory {
	
	private final AsmFactory asmFactory;
	
	public ObjectSetterFactory(final AsmFactory asmFactory) {
		this.asmFactory = asmFactory;
	}

	public <T, P> Setter<T, P> getSetter(final Class<? extends T> target, final String property) {
		// first look for method
		final Method method = lookForMethod(target, property);
		final Setter<T, P> setter;
		if (method == null) {
			setter = getFieldSetter(target, property);
		} else {
			setter = getMethodSetter(method);
		}
		return setter;
	}

	public <T, P> Setter<T, P> getMethodSetter(final Method method) {
		boolean accessible = Modifier.isPublic(method.getModifiers()) && Modifier.isPublic(method.getDeclaringClass().getModifiers());
		if (asmFactory != null && accessible) {
			try {
				return asmFactory.createSetter(method);
			} catch(Throwable e) {
                // ignore
			}
		}
		if (!accessible) {
			method.setAccessible(true);
		}
        return new MethodSetter<T, P>(method);
	}

	public <T, P> Setter<T, P> getFieldSetter(final Class<?> target, final String property) {
		// look for field
		final Field field = lookForField(target, property);
		
		if (field != null) {
            return getFieldSetter(field);
		} else {
			return null;
		}
	}

    public <T, P> Setter<T, P> getFieldSetter(Field field) {
		boolean accessible = Modifier.isPublic(field.getModifiers()) && Modifier.isPublic(field.getDeclaringClass().getModifiers());
		if (asmFactory != null && accessible) {
            try {
                return asmFactory.createSetter(field);
            } catch(Throwable e) {
            }
        }
        if (!accessible) {
            field.setAccessible(true);
        }
        return new FieldSetter<T, P>(field);
    }

    private Method lookForMethod(final Class<?> target, final String property) {
        if (target == null)  return null;

		for(Method m : target.getDeclaredMethods()) {
			if(SetterHelper.isSetter(m)
					&& SetterHelper.methodNameMatchesProperty(m.getName(), property)) {
				return m;
			}
		}
		
		if (!Object.class.equals(target)) {
			return lookForMethod(target.getSuperclass(), property);
		}
		
		return null;
	}
	

	private Field lookForField(final Class<?> target, final String property) {
        if (target == null)  return null;

		for(Field field : target.getDeclaredFields()) {
			if(SetterHelper.fieldModifiersMatches(field.getModifiers())
					&& SetterHelper.fieldNameMatchesProperty(field.getName(), property)) {
				return field;
			}
		}
		
		if (!Object.class.equals(target)) {
			return lookForField(target.getSuperclass(), property);
		}
		
		return null;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> BooleanSetter<T> toBooleanSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof BooleanSetter) {
			return (BooleanSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new BooleanMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new BooleanFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}

	private static boolean isNullSetter(Setter<?, ?> setter) {
		return setter == null || setter instanceof NullSetter;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> ByteSetter<T> toByteSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof ByteSetter) {
			return (ByteSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new ByteMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new ByteFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> CharacterSetter<T> toCharacterSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof CharacterSetter) {
			return (CharacterSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new CharacterMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new CharacterFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> ShortSetter<T> toShortSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof ShortSetter) {
			return (ShortSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new ShortMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new ShortFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> IntSetter<T> toIntSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof IntSetter) {
			return (IntSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new IntMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new IntFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> LongSetter<T> toLongSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof LongSetter) {
			return (LongSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new LongMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new LongFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> FloatSetter<T> toFloatSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof FloatSetter) {
			return (FloatSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new FloatMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new FloatFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, P> DoubleSetter<T> toDoubleSetter(final Setter<T, ? super P> setter) {
		if (isNullSetter(setter)) {
			return null;
		} else if (setter instanceof DoubleSetter) {
			return (DoubleSetter<T>) setter;
		} else if (setter instanceof MethodSetter) {
			return new DoubleMethodSetter<T>(((MethodSetter) setter).getMethod());
		} else if (setter instanceof FieldSetter) {
			return new DoubleFieldSetter<T>(((FieldSetter) setter).getField());
		} else {
			throw new IllegalArgumentException("Invalid type " + setter);
		}
	}

}