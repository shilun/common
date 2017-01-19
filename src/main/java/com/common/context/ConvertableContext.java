//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.context;

import com.common.context.Convertable;
import java.lang.reflect.Method;

public final class ConvertableContext<T, V> {
    private final Class<T> type;
    private final Class<V> valueType;
    private final Method valueMethod;
    private final Method ofMethod;
    private final Object[] zeroParameter = new Object[0];

    ConvertableContext(Class<T> type, Class<V> valueType, Method valueMethod, Method ofMethod) {
        this.type = type;
        this.valueType = valueType;
        this.valueMethod = valueMethod;
        this.ofMethod = ofMethod;
    }

    public Class<T> getType() {
        return this.type;
    }

    public Class<V> getValueType() {
        return this.valueType;
    }

    public V value(T source) throws Exception {
        return this.valueType.cast(this.valueMethod.invoke(source, this.zeroParameter));
    }

    public T of(V value) throws Exception {
        return this.type.cast(this.ofMethod.invoke((Object)null, new Object[]{value}));
    }

    public static <T, V> ConvertableContext<T, V> build(String className) throws RuntimeException {
        Class convertableClazz;
        try {
            convertableClazz = Class.forName(className);
        } catch (ClassNotFoundException var3) {
            throw new RuntimeException("Class not found", var3);
        }

        return build(convertableClazz);
    }

    public static <T, V> ConvertableContext<T, V> build(Class<T> type) throws RuntimeException {
        if(!type.isAnnotationPresent(Convertable.class)) {
            throw new RuntimeException("Class should be annotated by Convertable.");
        } else {
            Convertable convertable = (Convertable)type.getAnnotation(Convertable.class);

            Method valueMethod;
            try {
                valueMethod = type.getMethod(convertable.valueMethod(), new Class[0]);
            } catch (Exception var7) {
                throw new RuntimeException("Failed to obtain method:" + convertable.valueMethod() + "(which should have non-argument).", var7);
            }

            Class valueType = valueMethod.getReturnType();

            Method ofMethod;
            try {
                ofMethod = type.getMethod(convertable.ofMethod(), new Class[]{valueType});
            } catch (Exception var6) {
                throw new RuntimeException("Failed to obtain method:" + convertable.ofMethod() + "(which should have a(n) " + valueType.getName() + " typed argument)", var6);
            }

            return new ConvertableContext(type, valueType, valueMethod, ofMethod);
        }
    }
}
